package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResult;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResultBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslResultValueReader implements TransportReader<PtTlsMessageValueSaslResult>{

	private PtTlsMessageValueSaslResultBuilder builder;
	
	PtTlsMessageSaslResultValueReader(PtTlsMessageValueSaslResultBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueSaslResult read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueSaslResult mValue = null;
				PtTlsMessageValueSaslResultBuilder valueBuilder = (PtTlsMessageValueSaslResultBuilder)builder.clear();
				
				try{
					try{
						
						/* result */
						errorOffset = buffer.bytesRead();
						int result = buffer.readInt((byte)2);
						valueBuilder.setResult(result);
						
						/* optional result data */
						int counter = (int)(length - 2);
						if(counter > 0){
							errorOffset = buffer.bytesRead();
							byte[] resultData = buffer.read(counter);
							valueBuilder.setOptionalResultData(resultData);
						}
						

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueSaslResult)valueBuilder.toValue();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.SASL_RLT.length();
	}
}