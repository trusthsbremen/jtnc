package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponseBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageVersionResponseValueReader implements TransportReader<PtTlsMessageValueVersionResponse>{

	private PtTlsMessageValueVersionResponseBuilder builder;
	
	PtTlsMessageVersionResponseValueReader(PtTlsMessageValueVersionResponseBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueVersionResponse read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueVersionResponse mValue = null;
				PtTlsMessageValueVersionResponseBuilder valueBuilder = (PtTlsMessageValueVersionResponseBuilder)builder.clear();
				
				try{
					try{
						
						/* ignore reserved */
						errorOffset = buffer.bytesRead();
						buffer.read(3);
						
						/* selected version */
						errorOffset = buffer.bytesRead();
						short version = buffer.readShort((byte)1);
						valueBuilder.setVersion(version);

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueVersionResponse)valueBuilder.toValue();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.VER_RES.length();
	}
}
