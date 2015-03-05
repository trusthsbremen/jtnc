package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.DefaultTnccsBatchContainer;
import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoSkipOnUnknownMessage;
import org.ietf.nea.pb.validate.rules.PbMessageNoSkip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbReader implements TnccsReader<TnccsBatchContainer>, Combined<TnccsReader<PbMessageValue>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PbReader.class);
			
	private final TnccsReader<PbBatchHeader> bHeadReader;
	private final TnccsReader<PbMessageHeader> mHeadReader;
	private final Map<Long, Map<Long, TnccsReader<PbMessageValue>>> valueReader;
	
	PbReader(TnccsReader<PbBatchHeader> bHeadReader,
			TnccsReader<PbMessageHeader> mHeadReader) {
		this(bHeadReader, mHeadReader, new HashMap<Long, Map<Long, TnccsReader<PbMessageValue>>>());

	}
	
	
	public PbReader(TnccsReader<PbBatchHeader> bHeadReader,
			TnccsReader<PbMessageHeader> mHeadReader,
			Map<Long, Map<Long, TnccsReader<PbMessageValue>>> valueReader) {
		this.bHeadReader = bHeadReader;
		this.mHeadReader = mHeadReader;
		this.valueReader = valueReader;
	}

	@Override
	public TnccsBatchContainer read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException {
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		if(!buffer.isReadable()){
			throw new IllegalArgumentException("Buffer must be readable.");
		}
		
		List<ValidationException> minorExceptions = new LinkedList<>();
		
		/* batch header */
		PbBatchHeader bHead = null;
		
		byte[] headerByteCopy = buffer.read(this.getMinDataLength());
		
		if(headerByteCopy.length < this.getMinDataLength()){
			throw new SerializationException("Buffer does not contain enough data.",true, headerByteCopy.length);
		}
		
		ByteBuffer tempBuf = new DefaultByteBuffer(headerByteCopy.length);
		tempBuf.write(headerByteCopy);
		
		// ignore length here, because header has a length field.
		try{
			bHead = bHeadReader.read(tempBuf, -1);
		}catch(ValidationException e){
			throw new ValidationException(e.getMessage(), e.getCause(), e.getExceptionOffset(), headerByteCopy);
		}
		
		ByteBuffer fullBuffer = null;
		
		try{
			if(buffer.isReadable() && !buffer.isWriteable()){
				fullBuffer = new DefaultByteBuffer(length);
				fullBuffer.write(headerByteCopy); // copy the header data back in
				fullBuffer.write(buffer.read(length - this.getMinDataLength())); // get the rest of the data
				fullBuffer.read(headerByteCopy.length); // set correct position
			}else{
				fullBuffer = buffer;
			}
			
		}catch(BufferUnderflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}

		/* messages */
		List<PbMessage> msgs = new LinkedList<>();
		long contentLength = bHead.getLength() - PbMessageTlvFixedLengthEnum.BATCH.length();
		if(contentLength >= mHeadReader.getMinDataLength()){
			for(long cl = 0; cl < contentLength;){
				PbMessageHeader mHead = null;
				PbMessageValue mValue = null;
				long headerOffset = fullBuffer.bytesRead();
				try{
					// ignore length here because header has a length field
					mHead = mHeadReader.read(fullBuffer, -1);
					
					long vendor = mHead.getVendorId();
					long type = mHead.getMessageType();
					long valueLength = mHead.getLength()-PbMessageTlvFixedLengthEnum.MESSAGE.length();
					
					if(valueReader.containsKey(vendor)){
						if(valueReader.get(vendor).containsKey(type)){
							
							TnccsReader<PbMessageValue> vr = valueReader.get(vendor).get(type);
							
							// Do a length check before trying to parse the value.
							try{
								// If there is remaining data, the validation exception may be overlaid by
								// a serialization exception because the length is such a vital part.
								MinMessageLength.check(valueLength, vr.getMinDataLength());
							}catch(RuleException e1){
								// Add 8 to header offset because this is a late header check. Length is the third field
								// in the attribute header.
								throw new ValidationException(e1.getMessage(), e1,headerOffset + 8);
							}
							
							mValue = vr.read(fullBuffer, valueLength);
							
							if(mValue != null){
								// Now that the value is known do the no skip check.
								try{
									PbMessageNoSkip.check(mValue, mHead.getFlags());
								}catch(RuleException e1){
									// Use header offset because this is a late header check. Flags is the first field
									// in the attribute header.
									throw new ValidationException(e1.getMessage(), e1, headerOffset);
								}

								// filter messages of type access recommendation and assessment result if batch is not of type result. RFC 5793
								if(mHead.getVendorId() == IETFConstants.IETF_PEN_VENDORID && mHead.getMessageType() == PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id()){
									if(bHead.getType().equals(PbBatchTypeEnum.RESULT)){
										msgs.add(new PbMessage(mHead, mValue));
									}else{
										try{
											PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().id()).setLength(bHead.getLength() - mHead.getLength()).toObject();
											bHead = changedBHeader;
										}catch(RuleException e2){
											// the new header is only a convenient process if the creation fails
											// leave the old header in place and do nothing.
											LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
										}	
									}
								}else if(mHead.getVendorId() == IETFConstants.IETF_PEN_VENDORID && mHead.getMessageType() == PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id()){
									if(bHead.getType().equals(PbBatchTypeEnum.RESULT)){
										msgs.add(new PbMessage(mHead, mValue));
									}else{
										try{
											PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().id()).setLength(bHead.getLength() - mHead.getLength()).toObject();
											bHead = changedBHeader;
										}catch(RuleException e2){
											// the new header is only a convenient process if the creation fails
											// leave the old header in place and do nothing.
											LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
										}	
									}
								}else{
									msgs.add(new PbMessage(mHead, mValue));
								}
								
							} // if null you can ignore the message
							
						}else{
							try{
								NoSkipOnUnknownMessage.check(mHead.getFlags());
							}catch(RuleException e1){
								// Use header offset because this is a late header check. Flags is the first field
								// in the attribute header.
								throw new ValidationException(e1.getMessage(), e1,headerOffset);
							}
							
							try{
								LOGGER.warn("Vendor ID " + vendor + " with type "+ type +" not supported, attribute will be skipped.");
								// skip the remaining bytes of the attribute
								buffer.read(valueLength);
								try{
									PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().id()).setLength(bHead.getLength() - mHead.getLength()).toObject();
									bHead = changedBHeader;
								}catch(RuleException e2){
									// the new header is only a convenient process if the creation fails
									// leave the old header in place and do nothing.
									LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
								}
							}catch (BufferUnderflowException e){
								throw new SerializationException("Data length " +fullBuffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(fullBuffer.bytesWritten()));
							}
						}
					}else{
						try{
							NoSkipOnUnknownMessage.check(mHead.getFlags());
						}catch(RuleException e1){
							// Use header offset because this is a late header check. Flags is the first field
							// in the attribute header.
							throw new ValidationException(e1.getMessage(), e1,headerOffset);
						}
						
						try{
							LOGGER.warn("Vendor ID " + vendor + " with type "+ type +" not supported, attribute will be skipped.");
							// skip the remaining bytes of the attribute
							buffer.read(valueLength);
							try{
								PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().id()).setLength(bHead.getLength() - mHead.getLength()).toObject();
								bHead = changedBHeader;
							}catch(RuleException e2){
								// the new header is only a convenient process if the creation fails
								// leave the old header in place and do nothing.
								LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
							}
						}catch (BufferUnderflowException e){
							throw new SerializationException("Data length " +fullBuffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(fullBuffer.bytesWritten()));
						}
						
					}
				}catch(ValidationException e){
					
					RuleException t = e.getCause();
					if(t == null || t.isFatal()){
						
						throw e;
						
					}else{
				
						try{
							// skip the remaining bytes of the attribute
							// if you mess with the attribute length a bit this will throw a serialization exception.
							minorExceptions.add(e);
							fullBuffer.read(mHead.getLength() - (fullBuffer.bytesRead() - headerOffset));
							
							try{
								PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().id()).setLength(bHead.getLength() - mHead.getLength()).toObject();
								bHead = changedBHeader;
							}catch(RuleException e2){
								// the new header is only a convenient process if the creation fails
								// leave the old header in place and do nothing.
								LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
							}
							
						}catch (BufferUnderflowException e1){
							throw new SerializationException("Data length " +fullBuffer.bytesWritten()+ " in buffer to short.",e1,true, Long.toString(fullBuffer.bytesWritten()));
						}
					}
					
				}finally{
					cl += mHead.getLength();
				}
				
			}
		}
		
		try{
			BatchResultWithoutMessageAssessmentResult.check(bHead.getType(), msgs);
		}catch(RuleException e){
			throw new ValidationException(e.getMessage(), e, 0);
		}
		
		PbBatch b = new PbBatch(bHead,msgs);
		
		return new DefaultTnccsBatchContainer(b, minorExceptions);
	}

	@Override
	public byte getMinDataLength() {
		// batch header is minimal requirement
		return bHeadReader.getMinDataLength();
	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final TnccsReader<PbMessageValue> reader) {
		if(valueReader.containsKey(vendorId)){
			valueReader.get(vendorId).put(messageType, reader);
		}else{
			valueReader.put(vendorId, new HashMap<Long, TnccsReader<PbMessageValue>>());
			valueReader.get(vendorId).put(messageType, reader);
		}
		
	}

	@Override
	public void remove(final Long vendorId, final Long messageType) {
		if(valueReader.containsKey(vendorId)){
			if(valueReader.get(vendorId).containsKey(messageType)){
				valueReader.get(vendorId).remove(messageType);
			}
			if(valueReader.get(vendorId).isEmpty()){
				valueReader.remove(vendorId);
			}
		}
	}

}
