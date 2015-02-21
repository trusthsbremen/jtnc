package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueExperimental;
import org.ietf.nea.pb.message.PbMessageValueExperimentalBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageExperimentalValueReader implements TnccsReader<PbMessageValueExperimental>{

	private PbMessageValueExperimentalBuilder baseBuilder;
	
	PbMessageExperimentalValueReader(PbMessageValueExperimentalBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueExperimental read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueExperimental value = null;
		PbMessageValueExperimentalBuilder builder = (PbMessageValueExperimentalBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				/* message */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and my switch to a negative value.
				byte[] sData = buffer.read((int) messageLength);
				String message = new String(sData, Charset.forName("US-ASCII"));
				builder.setMessage(message);

			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueExperimental)builder.toObject();
			
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
