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
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.validate.rules.MinAttributeLength;
import org.ietf.nea.pa.validate.rules.NoSkipOnUnknownAttribute;
import org.ietf.nea.pa.validate.rules.PaAttributeNoSkip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.util.Combined;

class PaReader implements ImReader<PaMessage>, Combined<ImReader<PaAttributeValue>> {
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
	public PaMessage read(final InputStream in, final long length)
			throws SerializationException, ValidationException {
		
		BufferedInputStream bIn = (in instanceof BufferedInputStream)? (BufferedInputStream)in : new BufferedInputStream(in) ;
		
		/* message header */
		PaMessageHeader mHead = null;

		// ignore length here, because header has a fixed length.
		mHead = mHeadReader.read(bIn, -1);

		/* attributes */
		long contentLength = length - PaAttributeTlvFixedLength.MESSAGE.length();
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
					headerOffset = PaAttributeTlvFixedLength.ATTRIBUTE.length();
					
					long vendor = aHead.getVendorId();
					long type = aHead.getAttributeType();
	
					if(valueReader.containsKey(vendor)){
						if(valueReader.get(vendor).containsKey(type)){
							
							ImReader<PaAttributeValue> vr = valueReader.get(vendor).get(type);
							
							// Do a length check before trying to parse the value.
							try{
								long valueLength = aHead.getLength()-PaAttributeTlvFixedLength.ATTRIBUTE.length();
								MinAttributeLength.check(valueLength, vr.getMinDataLength());
							}catch(RuleException e1){
								// Remove 4 from header offset because this is a late header check. Length is the last field
								// in the attribute header.
								headerOffset -= 4;
								throw new ValidationException(e1.getMessage(), e1,0);
							}
							
							aValue = vr.read(cIn, aHead.getLength()-PaAttributeTlvFixedLength.ATTRIBUTE.length());
							
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
							} // if null you can ignore the message
							
						}else{
							try{
								NoSkipOnUnknownAttribute.check(aHead.getFlags());
							}catch(RuleException e1){
								// Remove header offset because this is a late header check. Flags is the first field
								// in the attribute header.
								headerOffset = 0;
								throw new ValidationException(e1.getMessage(), e1,0);
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
							throw new ValidationException(e1.getMessage(), e1, 0);
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
					long offset = cl + PaAttributeTlvFixedLength.MESSAGE.length() + headerOffset + e.getExceptionOffset();
					LOGGER.warn("Validation exception occured while processing attribute with vendor ID " + aHead.getVendorId() +" and type " + aHead.getAttributeType() + ". Offset: " + offset,e);
					
					Throwable t = e.getCause();
					if(t == null || !(t instanceof RuleException)){
						throw e;
					}else if(((RuleException)t).isFatal()){
						// Repack the exception with the fully calculated offset and throw it.
						throw new ValidationException(e.getMessage(), (RuleException)e.getCause(), offset);
					}else{
					
						try{
							// skip the remaining bytes of the attribute
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
		
		
		PaMessage b = new PaMessage(mHead,attributes);
		
		return b;
	}

	@Override
	public byte getMinDataLength() {
		// no minimal requirement
		return 0;
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
