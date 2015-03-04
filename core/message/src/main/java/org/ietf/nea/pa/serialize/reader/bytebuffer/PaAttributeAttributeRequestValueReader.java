package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequestBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeAttributeRequestValueReader implements ImReader<PaAttributeValueAttributeRequest>{

	private PaAttributeValueAttributeRequestBuilder baseBuilder;
	
	PaAttributeAttributeRequestValueReader(PaAttributeValueAttributeRequestBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueAttributeRequest read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueAttributeRequest value = null;
		PaAttributeValueAttributeRequestBuilder builder = (PaAttributeValueAttributeRequestBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				// minimum of one value must exist
				long counter = 0;
				do{
					// TODO error offset is vague because it cannot be calculated to the exact offset. 
					errorOffset = buffer.bytesRead();
					/* attribute values */
					long vendorId = 0; 
					long attributeId = 0;

					/* ignore reserved */
					buffer.readByte();
					counter ++;
					
					/* vendor ID */
					vendorId = buffer.readLong((byte)3);
					counter += 3;
					
					/* attribute type */
					attributeId = buffer.readLong((byte)4);
					counter += 4;

					builder.addReferences(new AttributeReferenceEntry(vendorId, attributeId));
					
				}while(messageLength - counter > 0);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueAttributeRequest)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {

		return PaAttributeTlvFixedLengthEnum.ATT_REQ.length(); 
	}
}
