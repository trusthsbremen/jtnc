package org.ietf.nea.pt.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.PtTlsMessageHeaderBuilder;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageHeaderReader implements TransportReader<PtTlsMessageHeader>{

	private PtTlsMessageHeaderBuilder baseBuilder;
	
	PtTlsMessageHeaderReader(PtTlsMessageHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PtTlsMessageHeader read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageHeader mHeader = null;
				PtTlsMessageHeaderBuilder builder = (PtTlsMessageHeaderBuilder)this.baseBuilder.newInstance();
				
				try{
					try{
						
						/* reserved */
						errorOffset = buffer.bytesRead();
						buffer.readByte();
						
						/* vendor ID */
						errorOffset = buffer.bytesRead();
						long vendorId = buffer.readLong((byte)3);
						builder.setVendorId(vendorId);
	
						/* message type */
						errorOffset = buffer.bytesRead();
						long messageType = buffer.readLong((byte)4);
						builder.setType(messageType);

						/* message length */
						errorOffset = buffer.bytesRead();
						long mLength = buffer.readLong((byte)4);
						builder.setLength(mLength);
						
						/* message identifier */
						errorOffset = buffer.bytesRead();
						long identifier = buffer.readLong((byte)4);
						builder.setIdentifier(identifier);

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mHeader = (PtTlsMessageHeader)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return mHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();
	}

}
