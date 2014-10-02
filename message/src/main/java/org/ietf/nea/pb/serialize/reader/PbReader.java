package org.ietf.nea.pb.serialize.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageNew;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.PbMessageNoSkip;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.util.Combined;

public class PbReader implements TnccsReader<PbBatch>, Combined<TnccsReader<PbMessageValue>> {

	private final TnccsReader<PbBatchHeader> bHeadReader;
	private final TnccsReader<PbMessageHeader> mHeadReader;
	private final Map<Long, Map<Long, TnccsReader<PbMessageValue>>> valueReader;
	
	public PbReader(TnccsReader<PbBatchHeader> bHeadReader,
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
	public PbBatch read(final InputStream in, final long length)
			throws SerializationException, ValidationException {
		
		BufferedInputStream bIn = (in instanceof BufferedInputStream)? (BufferedInputStream)in : new BufferedInputStream(in) ;
		
		/* batch header */
		PbBatchHeader bHead = null;
		try{
			// ignore length here, because header has a length field.
			bHead = bHeadReader.read(bIn, -1);
		}catch (ValidationException e){
			//TODO full exception handling
			long offset = e.getExceptionOffset();
			System.out.println("Error occured at offset: " + offset + "\n" + e.toString());
		}
		
		/* messages */
		List<PbMessageNew> msgs = new LinkedList<>();
		long contentLength = bHead.getLength() - PbMessageTlvFixedLength.BATCH.length();
		if(contentLength >= mHeadReader.getMinDataLength()){
			for(long cl = 0; cl < contentLength;){
				PbMessageHeader mHead = null;
				PbMessageValue mValue = null;
				long headerOffset = 0;
				try{
					// ignore length here because header has a length field
					mHead = mHeadReader.read(bIn, -1);
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
							
							mValue = vr.read(bIn, mHead.getLength()-PbMessageTlvFixedLength.MESSAGE.length());
							
							// Now that the value is known do the no skip check.
							try{
								PbMessageNoSkip.check(mValue, mHead.getFlags());
							}catch(RuleException e1){
								// Remove header offset because this is a late header check. Flags is the first field
								// in the message header.
								headerOffset = 0;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
							
							msgs.add(new PbMessageNew(mHead, mValue));
							
						}else{
							try{
								this.checkNoSkipOnUnknown(mHead);
							}catch(RuleException e1){
								// Remove header offset because this is a late header check. Flags is the first field
								// in the message header.
								headerOffset = 0;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
						}
					}else{
						try{
							this.checkNoSkipOnUnknown(mHead);
						}catch(RuleException e1){
							// Remove header offset because this is a late header check. Flags is the first field
							// in the message header.
							headerOffset = 0;
							throw new ValidationException(e1.getMessage(), e1, 0);
						}
					}
				}catch(ValidationException e){
					
					// current message position + batch header length + the message header offset (if already parsed) 
					// + the offset of the exception data 	
					long offset = cl + PbMessageTlvFixedLength.BATCH.length() + headerOffset + e.getExceptionOffset();
					System.out.println("Error occured at offset: " + offset + "\n" + e.toString());
					
					Throwable t = e.getCause();
					if(t == null || !(t instanceof RuleException)){
						throw e;
					}else if(((RuleException)t).isFatal()){
						// Repack the exception with the fully calculated offset and throw it.
						throw new ValidationException(e.getMessage(), e, offset);
					}else{
					
						try{
							// skip the remaining bytes of the message
							bIn.skip(mHead.getLength() - (e.getExceptionOffset() + headerOffset));
						}catch (IOException e1){
							throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
						}
					}
					
				}finally{
					cl += mHead.getLength();
				}
				
			}
		}
		
		PbBatch b = new PbBatch(bHead,msgs);
		
		return b;
	}

	private void checkNoSkipOnUnknown(PbMessageHeader mHead) throws RuleException {
		if(mHead.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
			throw new RuleException("Message is not supported but has No Skip set.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code(),PbErrorCauseEnum.NOT_SPECIFIED.number());
		}
	}

	@Override
	public byte getMinDataLength() {
		// no minimal requirement
		return 0;
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
