package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeProductInformationValueWriter implements ImWriter<PaAttributeValueProductInformation>{

	@Override
	public void write(final PaAttributeValueProductInformation data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueProductInformation aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* vendor id 24 bit(s) */
		byte[] vendorId = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getVendorId()).array(),5,8);
		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false,
					Long.toString(aValue.getVendorId()));
		}
		
		/* product id 16 bit(s) */
		byte[] productId = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(aValue.getProductId()).array(),2,4);
		try {
			buffer.write(productId);
		} catch (IOException e) {
			throw new SerializationException(
					"Product ID could not be written to the buffer.", e, false,
					Integer.toString(aValue.getProductId()));
		}
		
		/* product name variable bit(s) */
		byte [] productName = aValue.getName().getBytes(Charset.forName("UTF-8"));
		
		try {
			buffer.write(productName);
		} catch (IOException e) {
			throw new SerializationException("Product name could not be written to the buffer.",e,false, aValue.getName());
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
