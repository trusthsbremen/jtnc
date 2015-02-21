package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUriBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParameterUriSubValueReader implements TnccsReader<PbMessageValueRemediationParameterUri>{

	private  PbMessageValueRemediationParameterUriBuilder baseBuilder;
	
	PbMessageRemediationParameterUriSubValueReader( PbMessageValueRemediationParameterUriBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public  PbMessageValueRemediationParameterUri read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueRemediationParameterUri value = null;
		PbMessageValueRemediationParameterUriBuilder builder = (PbMessageValueRemediationParameterUriBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* URI */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)messageLength);
				String uriString = new String(sData, Charset.forName("US-ASCII"));
				builder.setUri(uriString);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = ( PbMessageValueRemediationParameterUri)builder.toObject();
			
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
