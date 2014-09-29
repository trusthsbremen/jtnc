package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageLanguagePreferenceSerializer implements TnccsSerializer<PbMessageValueLanguagePreference> {
	
	private PbMessageValueLanguagePreferenceBuilder builder;
	
	PbMessageLanguagePreferenceSerializer(PbMessageValueLanguagePreferenceBuilder builder){
	    	this.builder = builder;
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
					"Message could not be written to the buffer.", e, false, 0,
					data.getPreferedLanguage());
		}
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueLanguagePreference decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		PbMessageValueLanguagePreference value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;
		//String preferedLanguage = "Accept-Language: en";
		String preferedLanguage = "";
		byte[] buffer = new byte[0];
		byte[] temp = new byte[0];
		
		for(long l = messageLength; l > 0; l -= buffer.length){
			
			try{
				buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ?(int)l : 65535));
			}catch(IOException e){
				throw new SerializationException(
						"InputStream could not be read.", e, true, 0);
			}
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer, 0, buffer.length));
		}
		if(temp != null && temp.length > 0){
			preferedLanguage = new String(temp, Charset.forName("US-ASCII"));
		}
		
		this.builder.setLanguagePreference(preferedLanguage);
		
		value = (PbMessageValueLanguagePreference)this.builder.toValue();
		
		return value;
	}

}
