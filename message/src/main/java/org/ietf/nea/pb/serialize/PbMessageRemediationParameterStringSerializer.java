package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageRemediationParameterStringSerializer implements TnccsSerializer<PbMessageValueRemediationParameterString> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageValueRemediationParameterString.FIXED_LENGTH;
	
	private static final class Singleton{
		static final PbMessageRemediationParameterStringSerializer INSTANCE = new  PbMessageRemediationParameterStringSerializer();  
	}
	
	static  PbMessageRemediationParameterStringSerializer getInstance(){
	    	return Singleton.INSTANCE;
	}
	    
	private  PbMessageRemediationParameterStringSerializer(){
	    	// Singleton
	}
	
	
	@Override
	public void encode(final PbMessageValueRemediationParameterString data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		
		/* String length 32 bit(s)*/
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(data.getStringLength()).array(),4,8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e,
					Long.toString(data.getStringLength()));
		}
		
		/* Reason String*/
		try{
			buffer.write(data.getRemediationString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Reason could not be written to the buffer.", e,
					data.getRemediationString());
		}
		
		/* Language Code length 8 bit(s)*/
		buffer.write(data.getLangCodeLength());
		
		/* Language Code */
		try{
			buffer.write(data.getLangCode().getBytes(Charset.forName("US-ASCII")));
		} catch (IOException e) {
			throw new SerializationException(
					"Language could not be written to the buffer.", e,
					data.getLangCode());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
		}
	}

	@Override
	public PbMessageValueRemediationParameterString decode(final InputStream in, final long length) throws SerializationException {
		PbMessageValueRemediationParameterString value = null; 	

		// ignore any given length and find out on your own.
			
		// First 4 bytes are the remediation string length.
		String remediationString = readString((int)(MESSAGE_VALUE_FIXED_SIZE - 1), in, Charset.forName("UTF-8"));
		// Last 1 byte is the language code length.
		String langCode = readString((int)(MESSAGE_VALUE_FIXED_SIZE - 4), in, Charset.forName("US-ASCII"));
		
		// TODO unclean but right now no better solution.
		value = new PbMessageValueRemediationParameterString(remediationString, langCode);
		
		return value;
	}
		
	private String readString(final int length, final InputStream in, final Charset charset) throws SerializationException{
		
		String returnValue = "";
		
		int stringLength = length;
		
		byte[] buffer = new byte[stringLength];

		int count = 0;
		// wait till data is available
		while (count == 0) {
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
		}

		if (count >= stringLength){
			
			long contentLength =  ByteArrayHelper.toLong(Arrays.copyOfRange(buffer, 0, stringLength));
			
			buffer = new byte[0];
			byte[] temp = new byte[0];
			count = 0;
			
			for(; contentLength > 0; contentLength -= count){
				
				buffer = (stringLength < 65535) ? new byte[(int)stringLength] : new byte[65535];
				try {
					count = in.read(buffer);
				} catch (IOException e) {
					throw new SerializationException(
							"InputStream could not be read.", e);
				}
				
				temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer, 0, count));
			}
			if(temp != null && temp.length > 0){
				returnValue = new String(temp, charset);
			}
		}
		
		return returnValue;
	}

}
