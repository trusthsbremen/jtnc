package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUriBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeRemediationParameterUriValueReader implements ImReader<PaAttributeValueRemediationParameterUri>{

	private  PaAttributeValueRemediationParameterUriBuilder baseBuilder;
	
	PaAttributeRemediationParameterUriValueReader( PaAttributeValueRemediationParameterUriBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public  PaAttributeValueRemediationParameterUri read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		 PaAttributeValueRemediationParameterUri value = null;
		 PaAttributeValueRemediationParameterUriBuilder builder = (PaAttributeValueRemediationParameterUriBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* uri */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of 
				// the length is shortened here and my switch to a negative value
				byte[] sData = buffer.read((int)messageLength);
				String uriString = new String(sData, Charset.forName("US-ASCII"));
				builder.setUri(uriString);
				
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = ( PaAttributeValueRemediationParameterUri)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}


	@Override
	public byte getMinDataLength() {
		// no minimal length
		return 0; 
	}
}
