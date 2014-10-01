package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageReasonStringSerializer implements TnccsSerializer<PbMessageValueReasonString> {

	//private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageValueReasonString.FIXED_LENGTH;
 
	private PbMessageValueReasonStringBuilder builder;
	
	PbMessageReasonStringSerializer(PbMessageValueReasonStringBuilder builder){
		this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueReasonString data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		
		/* String length 32 bit(s)*/
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(data.getStringLength()).array(), 4,8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e, false, 0,
					Long.toString(data.getStringLength()));
		}
		
		/* reason String*/
		try{
			buffer.write(data.getReasonString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Reason could not be written to the buffer.", e, false, 0,
					data.getReasonString());
		}
		
		/* language code length 8 bit(s)*/
		buffer.write(data.getLangCodeLength());
		
		/* language code */
		try{
			buffer.write(data.getLangCode().getBytes(Charset.forName("US-ASCII")));
		} catch (IOException e) {
			throw new SerializationException(
					"Language could not be written to the buffer.", e, false, 0,
					data.getLangCode());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueReasonString decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		
		PbMessageValueReasonString value = null; 	
		this.builder.clear();
		// ignore any given length and find out on your own.
		
		// First 4 bytes are the reason string length.
		String reasonString = readString(4, in, Charset.forName("UTF-8"));
		this.builder.setReasonString(reasonString);
		
		// Last byte is the language code length;
		String langCode = readString(1, in, Charset.forName("US-ASCII"));
		this.builder.setLangCode(langCode);
		
		value = (PbMessageValueReasonString)this.builder.toValue();
		
		return value;
	}
		
	private String readString(final int length, final InputStream in, final Charset charset) throws SerializationException{
		
		String returnValue = "";
		
		int stringLength = length;
		
		byte[] buffer = new byte[0];

		try{
			buffer = ByteArrayHelper.arrayFromStream(in, stringLength);
		}catch(IOException e){
			throw new SerializationException(
					"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
		}

		if (buffer.length >= stringLength){
			
			long contentLength =  ByteArrayHelper.toLong(Arrays.copyOfRange(buffer, 0, stringLength));
			
			buffer = new byte[0];
			byte[] temp = new byte[0];
			
			for(; contentLength > 0; contentLength -= buffer.length){
				
				try{
					buffer = ByteArrayHelper.arrayFromStream(in, ((contentLength < 65535) ?(int)contentLength : 65535));
				}catch(IOException e){
					throw new SerializationException(
							"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
				}
				
				temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, buffer.length));

			}
			if(temp != null && temp.length > 0){
				returnValue = new String(temp, charset);
			}
		}
		
		return returnValue;
	}

}
