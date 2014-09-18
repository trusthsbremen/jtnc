package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageLanguagePreferenceSerializer implements TnccsSerializer<PbMessageValueLanguagePreference> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = 0;
	
	private static final class Singleton{
		private static final PbMessageLanguagePreferenceSerializer INSTANCE = new PbMessageLanguagePreferenceSerializer();  
	}
	public static  PbMessageLanguagePreferenceSerializer getInstance(){
	    	return Singleton.INSTANCE;
	}
	    
	private  PbMessageLanguagePreferenceSerializer(){
	    	// Singleton
	}
	
	
	@Override
	public void encode(final PbMessageValueLanguagePreference data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Message */
		try{
			buffer.write(data.getPreferedLanguage().getBytes(Charset.forName("US-ASCII")));
		} catch (IOException e) {
			throw new SerializationException(
					"Message could not be written to the buffer.", e,
					data.getPreferedLanguage());
		}
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
		}
	}

	@Override
	public PbMessageValueLanguagePreference decode(final InputStream in, final long length) throws SerializationException {
		PbMessageValueLanguagePreference value = null; 	
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;
		
		byte[] buffer = new byte[MESSAGE_VALUE_FIXED_SIZE];

		int count = 0;
		String preferedLanguage = "Accept-Language: en";
		
		count = 0;
		byte[] temp = new byte[0];
		for(long l = messageLength; l > 0; l -= count){
			
			buffer = (l < 65535) ? new byte[(int)l] : new byte[65535];
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer, 0, count));
		}
		if(temp != null && temp.length > 0){
			preferedLanguage = new String(temp, Charset.forName("US-ASCII"));
		}
		
		value = PbMessageValueBuilderIetf.createLanguagePreferenceValue(preferedLanguage);
		
		return value;
	}

}
