package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimental;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimentalBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageExperimentalValueReader implements TransportReader<PtTlsMessageValueExperimental>{

	private PtTlsMessageValueExperimentalBuilder builder;
	
	PtTlsMessageExperimentalValueReader(PtTlsMessageValueExperimentalBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueExperimental read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueExperimental mValue = null;
				PtTlsMessageValueExperimentalBuilder valueBuilder = (PtTlsMessageValueExperimentalBuilder)builder.clear();
				
				try{
					try{
						
						/* message */
						errorOffset = buffer.bytesRead();
						byte[] sData = buffer.read((int)length); 
						String message = new String(sData, Charset.forName("US-ASCII"));
						valueBuilder.setMessage(message);


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueExperimental)valueBuilder.toValue();
					
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
