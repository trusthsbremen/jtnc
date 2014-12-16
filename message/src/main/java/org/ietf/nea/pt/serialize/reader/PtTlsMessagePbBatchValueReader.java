package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatchBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessagePbBatchValueReader implements TransportReader<PtTlsMessageValuePbBatch>{

	private PtTlsMessageValuePbBatchBuilder builder;
	
	PtTlsMessagePbBatchValueReader(PtTlsMessageValuePbBatchBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValuePbBatch read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValuePbBatch mValue = null;
				PtTlsMessageValuePbBatchBuilder valueBuilder = (PtTlsMessageValuePbBatchBuilder)builder.clear();
				
				try{
					try{
						
						/* PB TNC batch */
						errorOffset = buffer.bytesRead();
						ByteBuffer data = buffer.read(length);
						valueBuilder.setTnccsData(data);

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValuePbBatch)valueBuilder.toValue();
					
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