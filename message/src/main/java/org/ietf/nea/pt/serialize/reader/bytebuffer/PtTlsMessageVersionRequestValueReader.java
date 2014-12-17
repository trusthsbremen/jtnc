package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequestBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageVersionRequestValueReader implements TransportReader<PtTlsMessageValueVersionRequest>{

	private PtTlsMessageValueVersionRequestBuilder baseBuilder;
	
	PtTlsMessageVersionRequestValueReader(PtTlsMessageValueVersionRequestBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PtTlsMessageValueVersionRequest read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueVersionRequest mValue = null;
				PtTlsMessageValueVersionRequestBuilder builder = (PtTlsMessageValueVersionRequestBuilder)this.baseBuilder.newInstance();
				
				try{
					try{
						
						/* ignore reserved */
						errorOffset = buffer.bytesRead();
						buffer.readByte();
						
						/* min version */
						errorOffset = buffer.bytesRead();
						short minVersion = buffer.readShort((byte)1);
						builder.setMinVersion(minVersion);
						
						/* max Version */
						errorOffset = buffer.bytesRead();
						short maxVersion = buffer.readShort((byte)1);
						builder.setMaxVersion(maxVersion);

						/* preferred Version */
						errorOffset = buffer.bytesRead();
						short preferredVersion = buffer.readShort((byte)1);
						builder.setPreferredVersion(preferredVersion);

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueVersionRequest)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.VER_REQ.length();
	}
}
