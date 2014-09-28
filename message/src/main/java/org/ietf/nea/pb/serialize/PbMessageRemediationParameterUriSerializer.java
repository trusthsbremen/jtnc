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

	private static final int MESSAGE_VALUE_FIXED_SIZE = 0;
	
	private PbMessageValueRemediationParameterUriBuilder builder;
	
	public PbMessageRemediationParameterUriSerializer(PbMessageValueRemediationParameterUriBuilder builder){
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
					"Message could not be written to the buffer.", e,
					data.getRemediationUri().toString());
		}
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
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
	
		byte[] buffer = new byte[MESSAGE_VALUE_FIXED_SIZE];

		int count = 0;
		String uri = "";
		
		count = 0;
		byte[] temp = new byte[0];
		for(long l= messageLength; l > 0; l -= count){
			
			buffer = (l < 65535) ? new byte[(int)l] : new byte[65535];
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, count));
		}
		if(temp != null && temp.length > 0){
			uri = new String(temp, Charset.forName("UTF-8"));
		}
		
		this.builder.setUri(uri);
		
		value = (PbMessageValueRemediationParameterUri)this.builder.toValue();
		
		return value;
	}

}
