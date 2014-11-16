package org.ietf.nea.pb.serialize.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.CountingInputStream;
import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchContainer;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoSkipOnUnknownMessage;
import org.ietf.nea.pb.validate.rules.PbMessageNoSkip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.util.Combined;

class PbReader implements TnccsReader<PbBatchContainer>, Combined<TnccsReader<PbMessageValue>> {

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
	public PbBatchContainer read(final InputStream in, final long length)
			throws SerializationException, ValidationException {
		
		BufferedInputStream bIn = (in instanceof BufferedInputStream)? (BufferedInputStream)in : new BufferedInputStream(in) ;
		List<ValidationException> minorExceptions = new LinkedList<>();
		
		/* batch header */
		PbBatchHeader bHead = null;

		// ignore length here, because header has a length field.
		bHead = bHeadReader.read(bIn, -1);

		/* messages */
		List<PbMessage> msgs = new LinkedList<>();
		long contentLength = bHead.getLength() - PbMessageTlvFixedLength.BATCH.length();
		if(contentLength >= mHeadReader.getMinDataLength()){
			for(long cl = 0; cl < contentLength;){
				PbMessageHeader mHead = null;
				PbMessageValue mValue = null;
				long headerOffset = 0;
				CountingInputStream cIn = new CountingInputStream(bIn);
				try{
					// ignore length here because header has a length field
					mHead = mHeadReader.read(cIn, -1);
					headerOffset = PbMessageTlvFixedLength.MESSAGE.length();
					
					long vendor = mHead.getVendorId();
					long type = mHead.getMessageType();
	
					if(valueReader.containsKey(vendor)){
						if(valueReader.get(vendor).containsKey(type)){
							
							TnccsReader<PbMessageValue> vr = valueReader.get(vendor).get(type);
							
							// Do a length check before trying to parse the value.
							try{
								long valueLength = mHead.getLength()-PbMessageTlvFixedLength.MESSAGE.length();
								MinMessageLength.check(valueLength, vr.getMinDataLength());
							}catch(RuleException e1){
								// Remove 4 from header offset because this is a late header check. Length is the last field
								// in the message header.
								headerOffset -= 4;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
							
							mValue = vr.read(cIn, mHead.getLength()-PbMessageTlvFixedLength.MESSAGE.length());
							
							if(mValue != null){
								// Now that the value is known do the no skip check.
								try{
									PbMessageNoSkip.check(mValue, mHead.getFlags());
								}catch(RuleException e1){
									// Remove header offset because this is a late header check. Flags is the first field
									// in the message header.
									headerOffset = 0;
									throw new ValidationException(e1.getMessage(), e1,0);
								}
								
								msgs.add(new PbMessage(mHead, mValue));
							} // if null you can ignore the message
							
						}else{
							try{
								NoSkipOnUnknownMessage.check(mHead.getFlags());
							}catch(RuleException e1){
								// Remove header offset because this is a late header check. Flags is the first field
								// in the message header.
								headerOffset = 0;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
							
							try{
								// skip the remaining bytes of the message
								cIn.skip(mHead.getLength() - headerOffset);
								try{
									PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().type()).setLength(bHead.getLength() - mHead.getLength()).toBatchHeader();
									bHead = changedBHeader;
								}catch(RuleException e2){
									// the new header is only a convenient process if the creation fails
									// leave the old header in place and do nothing.
									LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
								}
							}catch (IOException e1){
								throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
							}
						}
					}else{
						try{
							NoSkipOnUnknownMessage.check(mHead.getFlags());
						}catch(RuleException e1){
							// Remove header offset because this is a late header check. Flags is the first field
							// in the message header.
							headerOffset = 0;
							throw new ValidationException(e1.getMessage(), e1, 0);
						}
						
						try{
							// skip the remaining bytes of the message
							cIn.skip(mHead.getLength() - headerOffset);
							try{
								PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().type()).setLength(bHead.getLength() - mHead.getLength()).toBatchHeader();
								bHead = changedBHeader;
							}catch(RuleException e2){
								// the new header is only a convenient process if the creation fails
								// leave the old header in place and do nothing.
								LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
							}
						}catch (IOException e1){
							throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
						}
						
					}
				}catch(ValidationException e){
					
					// current message position + batch header length + the message header offset (if already parsed) 
					// + the offset of the exception data 	
					long offset = cl + PbMessageTlvFixedLength.BATCH.length() + headerOffset + e.getExceptionOffset();
					ValidationException updatedException = new ValidationException(e.getMessage(), e.getCause(), offset, e.getReasons());
					
					RuleException t = updatedException.getCause();
					if(t == null){
						throw updatedException;
					}else if(t.isFatal()){
						// Repack the exception with the fully calculated offset and throw it.
						throw updatedException;
					}else{
					
						try{
							// skip the remaining bytes of the message
							minorExceptions.add(updatedException);
							cIn.skip(mHead.getLength() - cIn.getByteCount());
							try{
								PbBatchHeader changedBHeader = (PbBatchHeader)new PbBatchHeaderBuilderIetf().setVersion(bHead.getVersion()).setDirection(bHead.getDirectionality().toDirectionalityBit()).setType(bHead.getType().type()).setLength(bHead.getLength() - mHead.getLength()).toBatchHeader();
								bHead = changedBHeader;
							}catch(RuleException e2){
								// the new header is only a convenient process if the creation fails
								// leave the old header in place and do nothing.
								LOGGER.warn("The header update for the current batch failed. The old header is left in use.");
							}
						}catch (IOException e1){
							throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
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
		
		return new PbBatchContainer(b, minorExceptions);
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
