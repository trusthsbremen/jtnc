package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.message.DefaultImMessageContainer;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.validate.rules.MinAttributeLength;
import org.ietf.nea.pa.validate.rules.NoSkipOnUnknownAttribute;
import org.ietf.nea.pa.validate.rules.PaAttributeNoSkip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.Combined;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaReader implements ImReader<ImMessageContainer>, Combined<ImReader<PaAttributeValue>> {
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
	public ImMessageContainer read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException {
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		if(!buffer.isReadable()){
			throw new IllegalArgumentException("Buffer must be readable.");
		}
		
		List<ValidationException> minorExceptions = new LinkedList<>();
		
		/* message header */
		PaMessageHeader mHead = null;
		
		byte[] headerByteCopy = buffer.read(this.getMinDataLength());
		
		if(headerByteCopy.length < this.getMinDataLength()){
			throw new SerializationException("Buffer does not contain enough data.",true, headerByteCopy.length);
		}
		
		ByteBuffer tempBuf = new DefaultByteBuffer(headerByteCopy.length);
		tempBuf.write(headerByteCopy);
		
		// ignore length here, because header has a length field.
		try{
			mHead = mHeadReader.read(tempBuf, -1);
		}catch(ValidationException e){
			throw new ValidationException(e.getMessage(), e.getCause(), e.getExceptionOffset(), headerByteCopy);
		}
		
		ByteBuffer fullBuffer = null;
		
		try{
			if(buffer.isReadable() && !buffer.isWriteable()){
				fullBuffer = new DefaultByteBuffer(length);
				fullBuffer.write(headerByteCopy); // copy the header data back in
				fullBuffer.write(buffer.read(length-this.getMinDataLength())); // get the rest of the data
				fullBuffer.read(headerByteCopy.length); // set correct position
			}else{
				fullBuffer = buffer;
			}
			
		}catch(BufferUnderflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}

		/* attributes */
		long contentLength = length - PaAttributeTlvFixedLengthEnum.MESSAGE.length();
		List<PaAttribute> attributes = new LinkedList<>();
		if(contentLength >= aHeadReader.getMinDataLength()){
			for(long cl = 0; cl < contentLength;){
				PaAttributeHeader aHead = null;
				PaAttributeValue aValue = null;
				long headerOffset = fullBuffer.bytesRead();
				try{

					// ignore length here because header has a length field
					aHead = aHeadReader.read(fullBuffer, -1);
					
					long vendor = aHead.getVendorId();
					long type = aHead.getAttributeType();
					long valueLength = aHead.getLength()-PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
					
					if(valueReader.containsKey(vendor)){
						if(valueReader.get(vendor).containsKey(type)){
							
							ImReader<PaAttributeValue> vr = valueReader.get(vendor).get(type);
							
							// Do a length check before trying to parse the value.
							try{
								// If there is remaining data, the validation exception may be overlaid by
								// a serialization exception because the length is such a vital part.
								MinAttributeLength.check(valueLength, vr.getMinDataLength());
							}catch(RuleException e1){
								// Add 8 to header offset because this is a late header check. Length is the third field
								// in the attribute header.
								throw new ValidationException(e1.getMessage(), e1,headerOffset + 8);
							}
							
							aValue = vr.read(fullBuffer,valueLength);
							
							if(aValue != null){
								// Now that the value is known do the no skip check.
								try{
									PaAttributeNoSkip.check(aValue, aHead.getFlags());
								}catch(RuleException e1){
									// Use header offset because this is a late header check. Flags is the first field
									// in the attribute header.
									throw new ValidationException(e1.getMessage(), e1, headerOffset);
								}
								
								attributes.add(new PaAttribute(aHead, aValue));
							} // if null you can ignore the attribute
							
						}else{
							try{
								NoSkipOnUnknownAttribute.check(aHead.getFlags());
							}catch(RuleException e1){
								// Use header offset because this is a late header check. Flags is the first field
								// in the attribute header.
								throw new ValidationException(e1.getMessage(), e1,headerOffset, aHead);
							}
							
							try{
								
								LOGGER.warn("Vendor ID " + vendor + " with type "+ type +" not supported, attribute will be skipped.");
								// skip the remaining bytes of the attribute
								fullBuffer.read(valueLength);
								
							}catch (BufferUnderflowException e){
								throw new SerializationException("Data length " +fullBuffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(fullBuffer.bytesWritten()));
							}
						}
					}else{
						
						try{
							NoSkipOnUnknownAttribute.check(aHead.getFlags());
						}catch(RuleException e1){
							// Use header offset because this is a late header check. Flags is the first field
							// in the attribute header.
							headerOffset = 0;
							throw new ValidationException(e1.getMessage(), e1, headerOffset, aHead);
						}
						
						try{
							
							LOGGER.warn("Vendor ID " + vendor + " with type "+ type +" not supported, attribute will be skipped.");
							// skip the remaining bytes of the attribute
							fullBuffer.read(valueLength);
							
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
							fullBuffer.read(aHead.getLength() - (fullBuffer.bytesRead() - headerOffset));
						}catch (BufferUnderflowException e1){
							throw new SerializationException("Data length " +fullBuffer.bytesWritten()+ " in buffer to short.",e1,true, Long.toString(fullBuffer.bytesWritten()));
						}
						
					}
					
				}finally{
					cl += aHead.getLength();
				}
				
			}
		}
		
		long l = 0;
		for (PaAttribute attr : attributes) {
			l += attr.getHeader().getLength();
		}
		
		l+=PaAttributeTlvFixedLengthEnum.MESSAGE.length();
		mHead.setLength(l);
		
		PaMessage m = new PaMessage(mHead,attributes);
		
		return new DefaultImMessageContainer(m, minorExceptions);
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
