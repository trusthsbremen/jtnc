package org.ietf.nea.pb.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterStringBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParameterStringSubValueReader implements TnccsReader<PbMessageValueRemediationParameterString>{

	private PbMessageValueRemediationParameterStringBuilder baseBuilder;
	
	PbMessageRemediationParameterStringSubValueReader(PbMessageValueRemediationParameterStringBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueRemediationParameterString read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Stream cannot be null.", in);
	    
		long errorOffset = 0;
		
		PbMessageValueRemediationParameterString value = null;
		PbMessageValueRemediationParameterStringBuilder builder = (PbMessageValueRemediationParameterStringBuilder)baseBuilder.newInstance();

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
				builder.setRemediationString(reasonString);
				errorOffset += reasonLength;
				
				// Last byte is the language code length;
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long langLength =  ByteArrayHelper.toLong(buffer);
				errorOffset += byteSize;
				
				String langCode = readString(langLength, in, Charset.forName("US-ASCII"));
				builder.setLangCode(langCode);
				errorOffset += langLength;
			
			}catch (IOException e){
				throw new SerializationException("Returned data for message value is to short or stream may be closed.",e,true);
			}

			value = (PbMessageValueRemediationParameterString)builder.toObject();
			
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
	
		return PbMessageTlvFixedLengthEnum.REA_STR_VALUE.length(); 
	}
}
