package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformationBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeProductInformationValueReader implements ImReader<PaAttributeValueProductInformation>{

	private PaAttributeValueProductInformationBuilder builder;
	
	PaAttributeProductInformationValueReader(PaAttributeValueProductInformationBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValueProductInformation read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueProductInformation value = null;
		builder = (PaAttributeValueProductInformationBuilder)builder.clear();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long vendorId = ByteArrayHelper.toLong(buffer);
				this.builder.setVendorId(vendorId);
				errorOffset += byteSize;
			
				/* product id */ 
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				int productId = ByteArrayHelper.toInt(buffer);
				this.builder.setProductId(productId);
				errorOffset += byteSize;
				
				/* product name */
				long nameLength = messageLength - PaAttributeTlvFixedLength.PRO_INF.length();
				String productName = this.readString(nameLength, in, Charset.forName("UTF-8"));
				this.builder.setName(productName);
				errorOffset += nameLength;

			}catch (IOException e){
				throw new SerializationException(
						"Returned data for attribute value is to short or stream may be closed.", e, true);
			}

			value = (PaAttributeValueProductInformation)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	private String readString(final long length, final InputStream in, final Charset charset) throws IOException{
		
		String returnValue = "";

		byte[] buffer = new byte[0];
		byte[] temp = new byte[0];
		
		for(long l = length; l > 0; l -= buffer.length){

			buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ? (int)l : 65535));
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, buffer.length));

		}
		
		if(temp != null && temp.length > 0){
			returnValue = new String(temp, charset);
		}
		
		return returnValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLength.PRO_INF.length();
	}

}
