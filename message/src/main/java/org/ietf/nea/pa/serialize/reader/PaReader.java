package org.ietf.nea.pa.serialize.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.CountingInputStream;
import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageContainer;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.validate.rules.MinAttributeLength;
import org.ietf.nea.pa.validate.rules.NoSkipOnUnknownAttribute;
import org.ietf.nea.pa.validate.rules.PaAttributeNoSkip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.Combined;

class PaReader implements ImReader<PaMessageContainer>, Combined<ImReader<PaAttributeValue>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaReader.class);
	
	private final ImReader<PaMessageHeader> mHeadReader;
	private final ImReader<PaAttributeHeader> aHeadReader;
	private final Map<Long, Map<Long, ImReader<PaAttributeValue>>> valueReader;
	
	PaReader(ImReader<PaMessageHeader> mHeadReader,
			ImReader<PaAttributeHeader> aHeadReader) {
		this(mHeadReader, aHeadReader, new HashMap<Long, Map<Long, ImReader<PaAttributeValue>>>());

	}
	
	public PaReader(ImReader<PaMessageHeader> mHeadReader,
			ImReader<PaAttributeHeader> aHeadReader,
			Map<Long, Map<Long, ImReader<PaAttributeValue>>> valueReader) {
		this.mHeadReader = mHeadReader;
		this.aHeadReader = aHeadReader;
		this.valueReader = valueReader;
	}

	@Override
	public PaMessageContainer read(final InputStream in, final long length)
			throws SerializationException, ValidationException {
		
		BufferedInputStream bIn = (in instanceof BufferedInputStream)? (BufferedInputStream)in : new BufferedInputStream(in) ;
		List<ValidationException> minorExceptions = new LinkedList<>();
		
		
		/* message header */
		PaMessageHeader mHead = null;

		// ignore length here, because header has a fixed length.
		mHead = mHeadReader.read(bIn, -1);

		/* attributes */
		long contentLength = length - PaAttributeTlvFixedLengthEnum.MESSAGE.length();
		List<PaAttribute> attributes = new LinkedList<>();
		if(contentLength >= aHeadReader.getMinDataLength()){
			for(long cl = 0; cl < contentLength;){
				PaAttributeHeader aHead = null;
				PaAttributeValue aValue = null;
				long headerOffset = 0;
				CountingInputStream cIn = new CountingInputStream(bIn);
				try{
					// ignore length here because header has a length field
					aHead = aHeadReader.read(cIn, -1);
					headerOffset = PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
					
					long vendor = aHead.getVendorId();
					long type = aHead.getAttributeType();
	
					if(valueReader.containsKey(vendor)){
						if(valueReader.get(vendor).containsKey(type)){
							
							ImReader<PaAttributeValue> vr = valueReader.get(vendor).get(type);
							
							// Do a length check before trying to parse the value.
							try{
								long valueLength = aHead.getLength()-PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
								MinAttributeLength.check(valueLength, vr.getMinDataLength());
							}catch(RuleException e1){
								// Remove 4 from header offset because this is a late header check. Length is the last field
								// in the attribute header.
								headerOffset -= 4;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
							
							aValue = vr.read(cIn, aHead.getLength()-PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length());
							
							if(aValue != null){
								// Now that the value is known do the no skip check.
								try{
									PaAttributeNoSkip.check(aValue, aHead.getFlags());
								}catch(RuleException e1){
									// Remove header offset because this is a late header check. Flags is the first field
									// in the attribute header.
									headerOffset = 0;
									throw new ValidationException(e1.getMessage(), e1,0);
								}
								
								attributes.add(new PaAttribute(aHead, aValue));
							} // if null you can ignore the attribute
							
						}else{
							try{
								NoSkipOnUnknownAttribute.check(aHead.getFlags());
							}catch(RuleException e1){
								// Remove header offset because this is a late header check. Flags is the first field
								// in the attribute header.
								headerOffset = 0;
								throw new ValidationException(e1.getMessage(), e1,0, aHead);
							}
							
							try{
								LOGGER.warn("Vendor ID " + vendor + " with type "+ type +" not supported, attribute will be skipped.");
								// skip the remaining bytes of the attribute
								cIn.skip(aHead.getLength() - headerOffset);
							}catch (IOException e1){
								throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
							}
						}
					}else{
						try{
							NoSkipOnUnknownAttribute.check(aHead.getFlags());
						}catch(RuleException e1){
							// Remove header offset because this is a late header check. Flags is the first field
							// in the attribute header.
							headerOffset = 0;
							throw new ValidationException(e1.getMessage(), e1, 0, aHead);
						}
						
						try{
							LOGGER.warn("Vendor ID " + vendor + " not supported, attribute will be skipped.");
							// skip the remaining bytes of the attribute
							cIn.skip(aHead.getLength() - headerOffset);
						}catch (IOException e1){
							throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
						}
						
					}
				}catch(ValidationException e){
					
					// current attribute position + message header length + the attribute header offset (if already parsed) 
					// + the offset of the exception data 	
					long offset = cl + PaAttributeTlvFixedLengthEnum.MESSAGE.length() + headerOffset + e.getExceptionOffset();
					ValidationException updatedException = new ValidationException(e.getMessage(), e.getCause(), offset, e.getReasons());
					
					RuleException t = updatedException.getCause();
					if(t == null){
						throw updatedException;
					}else if(t.isFatal()){
						// Repack the exception with the fully calculated offset and throw it.
						throw updatedException;
					}else{
					
						try{
							// skip the remaining bytes of the attribute
							minorExceptions.add(updatedException);
							cIn.skip(aHead.getLength() - (cIn.getByteCount()));
						}catch (IOException e1){
							throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
						}
					}
					
				}finally{
					cl += aHead.getLength();
				}
				
			}
		}
		
		
		PaMessage m = new PaMessage(mHead,attributes);
		
		return new PaMessageContainer(m, minorExceptions);
	}

	@Override
	public byte getMinDataLength() {
		// message header is minimal data length
		return mHeadReader.getMinDataLength();
	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final ImReader<PaAttributeValue> reader) {
		if(valueReader.containsKey(vendorId)){
			valueReader.get(vendorId).put(messageType, reader);
		}else{
			valueReader.put(vendorId, new HashMap<Long, ImReader<PaAttributeValue>>());
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
