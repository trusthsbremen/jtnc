package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueError;
import org.ietf.nea.pt.value.PtTlsMessageValueErrorBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageErrorValueReader implements TransportReader<PtTlsMessageValueError>{

	private PtTlsMessageValueErrorBuilder builder;
	
	PtTlsMessageErrorValueReader(PtTlsMessageValueErrorBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageValueError read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageValueError mValue = null;
				PtTlsMessageValueErrorBuilder valueBuilder = (PtTlsMessageValueErrorBuilder)builder.clear();
				
				try{
					try{
						
						/* reserved */
						errorOffset = buffer.bytesRead();
						buffer.readByte();
						
						/* vendor ID */
						errorOffset = buffer.bytesRead();
						long vendorId = buffer.readLong((byte)3);
						valueBuilder.setErrorVendorId(vendorId);
	
						/* error code */
						errorOffset = buffer.bytesRead();
						long errorCode = buffer.readLong((byte)4);
						valueBuilder.setErrorCode(errorCode);

						int counter = (int)(length - 8);
						if(counter > 0){
							errorOffset = buffer.bytesRead();
							byte[] messageCopy = buffer.read(counter);
							valueBuilder.setPartialMessage(messageCopy);
						}
						

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mValue = (PtTlsMessageValueError)valueBuilder.toValue();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mValue;
	}
	
	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length();
	}
}
