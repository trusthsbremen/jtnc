package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationDataBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslAuthenticationDataValueReader implements TransportReader<PtTlsMessageValueSaslAuthenticationData>{

	private PtTlsMessageValueSaslAuthenticationDataBuilder builder;
	
	PtTlsMessageSaslAuthenticationDataValueReader(PtTlsMessageValueSaslAuthenticationDataBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueSaslAuthenticationData read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueSaslAuthenticationData mValue = null;
				PtTlsMessageValueSaslAuthenticationDataBuilder valueBuilder = (PtTlsMessageValueSaslAuthenticationDataBuilder)builder.clear();
				
				try{
					try{
						
						/* authentication data */
						errorOffset = buffer.bytesRead();
						byte[] data = buffer.read((int)length); 
						valueBuilder.setAuthenticationData(data);


					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueSaslAuthenticationData)valueBuilder.toValue();
					
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
