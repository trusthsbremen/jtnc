package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUriBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageRemediationParameterUriSerializer implements TnccsSerializer<PbMessageValueRemediationParameterUri> {
	
	private PbMessageValueRemediationParameterUriBuilder builder;
	
	PbMessageRemediationParameterUriSerializer(PbMessageValueRemediationParameterUriBuilder builder){
	    	this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueRemediationParameterUri data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Message */
		try{
			buffer.write(data.getRemediationUri().toString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Message could not be written to the buffer.", e, false, 0,
					data.getRemediationUri().toString());
		}
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueRemediationParameterUri decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		
		PbMessageValueRemediationParameterUri value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;
		String uri = "";
		byte[] buffer = new byte[0];
		byte[] temp = new byte[0];
		
		for(long l= messageLength; l > 0; l -= buffer.length){
			
			
			try{
				buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ?(int)l : 65535));
			}catch(IOException e){
				throw new SerializationException(
						"InputStream could not be read.", e, true, 0);
			}
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, buffer.length));
		}
		if(temp != null && temp.length > 0){
			uri = new String(temp, Charset.forName("UTF-8"));
		}
		
		this.builder.setUri(uri);
		
		value = (PbMessageValueRemediationParameterUri)this.builder.toValue();
		
		return value;
	}

}
