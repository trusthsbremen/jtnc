package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimental;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimentalBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageExperimentalValueReader implements TransportReader<PtTlsMessageValueExperimental>{

	private PtTlsMessageValueExperimentalBuilder baseBuilder;
	
	PtTlsMessageExperimentalValueReader(PtTlsMessageValueExperimentalBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PtTlsMessageValueExperimental read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueExperimental mValue = null;
				PtTlsMessageValueExperimentalBuilder builder = (PtTlsMessageValueExperimentalBuilder)baseBuilder.newInstance();
				
				try{
					try{
						
						/* message */
						errorOffset = buffer.bytesRead();
						byte[] sData = buffer.read((int)length); 
						String message = new String(sData, Charset.forName("US-ASCII"));
						builder.setMessage(message);


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueExperimental)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return 0;
	}
}
