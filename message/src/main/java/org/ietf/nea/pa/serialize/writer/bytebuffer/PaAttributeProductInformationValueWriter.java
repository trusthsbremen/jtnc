package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeProductInformationValueWriter implements ImWriter<PaAttributeValueProductInformation>{

	@Override
	public void write(final PaAttributeValueProductInformation data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueProductInformation aValue = data;

		try{
			/* vendor id 24 bit(s) */
			buffer.writeDigits(aValue.getVendorId(), (byte)3);
			
			/* product id 16 bit(s) */
			buffer.writeUnsignedShort(aValue.getProductId());
			
			
			/* product name variable bit(s) */
			buffer.write(aValue.getName().getBytes(Charset.forName("UTF-8")));
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
