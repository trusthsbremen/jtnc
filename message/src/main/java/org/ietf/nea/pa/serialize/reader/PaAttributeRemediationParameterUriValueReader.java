package org.ietf.nea.pa.serialize.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUriBuilder;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.m.serialize.ImReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PaAttributeRemediationParameterUriValueReader implements ImReader<PaAttributeValueRemediationParameterUri>{

	private  PaAttributeValueRemediationParameterUriBuilder builder;
	
	PaAttributeRemediationParameterUriValueReader( PaAttributeValueRemediationParameterUriBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public  PaAttributeValueRemediationParameterUri read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		 PaAttributeValueRemediationParameterUri value = null;
		builder = ( PaAttributeValueRemediationParameterUriBuilder)builder.clear();

		try{
			
			try{
				// not needed here
				// int byteSize = 0;
				// byte[] buffer = new byte[byteSize];
				
				/* uri */
				String uriString = readString(messageLength, in, Charset.forName("US-ASCII"));
				this.builder.setUri(uriString);
				errorOffset += messageLength;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for batch header is to short or stream may be closed.",e,true);
			}

			value = ( PaAttributeValueRemediationParameterUri)builder.toValue();
			
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
		// no minimal length
		return 0; 
	}
}
