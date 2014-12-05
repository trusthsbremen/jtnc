package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterStringBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PaAttributeRemediationParameterStringValueReader implements ImReader<PaAttributeValueRemediationParameterString>{

	private PaAttributeValueRemediationParameterStringBuilder builder;
	
	PaAttributeRemediationParameterStringValueReader(PaAttributeValueRemediationParameterStringBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PaAttributeValueRemediationParameterString read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueRemediationParameterString value = null;
		builder = (PaAttributeValueRemediationParameterStringBuilder)builder.clear();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				// First 4 bytes are the reason string length.
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long reasonLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				String reasonString = readString(reasonLength, in, Charset.forName("UTF-8"));
				this.builder.setRemediationString(reasonString);
				errorOffset += reasonLength;
				
				// Last byte is the language code length;
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long langLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				String langCode = readString(langLength, in, Charset.forName("US-ASCII"));
				this.builder.setLangCode(langCode);
				errorOffset += langLength;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for attribute value is to short or stream may be closed.",e,true);
			}

			value = (PaAttributeValueRemediationParameterString)builder.toValue();
			
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
	
		return PaAttributeTlvFixedLength.REM_PAR_STR.length(); 
	}
}
