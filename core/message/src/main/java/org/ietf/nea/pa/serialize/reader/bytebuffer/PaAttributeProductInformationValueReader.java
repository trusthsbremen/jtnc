package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformationBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeProductInformationValueReader implements ImReader<PaAttributeValueProductInformation>{

	private PaAttributeValueProductInformationBuilder baseBuilder;
	
	PaAttributeProductInformationValueReader(PaAttributeValueProductInformationBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueProductInformation read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueProductInformation value = null;
		PaAttributeValueProductInformationBuilder builder = (PaAttributeValueProductInformationBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* vendor ID */
				errorOffset = buffer.bytesRead();
				long vendorId = buffer.readLong((byte)3);
				builder.setVendorId(vendorId);
			
				/* product id */ 
				errorOffset = buffer.bytesRead();
				int productId = buffer.readInt((byte)2);
				builder.setProductId(productId);
				
				/* product name */
				errorOffset = buffer.bytesRead();
				long nameLength = messageLength - PaAttributeTlvFixedLengthEnum.PRO_INF.length();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)nameLength);
				String productName = new String(sData, Charset.forName("UTF-8"));
				builder.setName(productName);

			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueProductInformation)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}
	
	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.PRO_INF.length();
	}

}
