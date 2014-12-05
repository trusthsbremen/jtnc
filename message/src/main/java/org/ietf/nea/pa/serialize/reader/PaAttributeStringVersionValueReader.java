package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersionBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeStringVersionValueReader implements ImReader<PaAttributeValueStringVersion>{

	private PaAttributeValueStringVersionBuilder builder;
	
	PaAttributeStringVersionValueReader(PaAttributeValueStringVersionBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValueStringVersion read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueStringVersion value = null;
		builder = (PaAttributeValueStringVersionBuilder)builder.clear();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				// First 4 bytes are the string length.
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long productLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				/* product version number */
				String productVersion = readString(productLength, in, Charset.forName("UTF-8"));
				this.builder.setProductVersion(productVersion);
				errorOffset += productLength;
				
				// First 4 bytes are the string length.
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long buildLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				/* product version number */
				String buildNumber = readString(buildLength, in, Charset.forName("UTF-8"));
				this.builder.setBuildNumber(buildNumber);
				errorOffset += buildLength;
				
				// First 4 bytes are the string length.
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long configLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				/* product version number */
				String configVersion = readString(configLength, in, Charset.forName("UTF-8"));
				this.builder.setConfigurationVersion(configVersion);
				errorOffset += configLength;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueStringVersion)builder.toValue();
			
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
	
		return PaAttributeTlvFixedLength.STR_VER.length(); 
	}
}
