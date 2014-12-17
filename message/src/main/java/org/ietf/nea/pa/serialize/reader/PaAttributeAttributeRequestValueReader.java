package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequestBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeAttributeRequestValueReader implements ImReader<PaAttributeValueAttributeRequest>{

	private PaAttributeValueAttributeRequestBuilder baseBuilder;
	
	PaAttributeAttributeRequestValueReader(PaAttributeValueAttributeRequestBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueAttributeRequest read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueAttributeRequest value = null;
		PaAttributeValueAttributeRequestBuilder builder = (PaAttributeValueAttributeRequestBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];

				// minimum of one value must exist
				do{
					/* attribute values */
					long vendorId = 0; 
					long attributeId = 0;
					int count = 0;
					/* ignore reserved */
					byteSize = 1;
					ByteArrayHelper.arrayFromStream(in, byteSize);
					count += byteSize;
					
					/* vendor ID */
					byteSize = 3;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					vendorId = ByteArrayHelper.toLong(buffer);
					count += byteSize;
					
					/* attribute type */
					byteSize = 4;
					buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
					attributeId = ByteArrayHelper.toLong(buffer);
					count += byteSize;

					builder.addReferences(new AttributeReference(vendorId, attributeId));
					
					// TODO error offset is vague because it cannot be calculated to the exact offset. 
					
					errorOffset += count;
					
				}while(messageLength - errorOffset > 0);
			
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
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
