package org.ietf.nea.pt.serialize.reader;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.PtTlsMessageHeaderBuilder;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageHeaderReader implements TransportReader<PtTlsMessageHeader>{

	private PtTlsMessageHeaderBuilder builder;
	
	PtTlsMessageHeaderReader(PtTlsMessageHeaderBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PtTlsMessageHeader read(final ByteBuffer buffer, final long length)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PtTlsMessageHeader mHeader = null;
				PtTlsMessageHeaderBuilder headerBuilder = (PtTlsMessageHeaderBuilder)builder.clear();
				
				try{
					try{
						
						/* reserved */
						errorOffset = buffer.bytesRead();
						buffer.readByte();
						
						/* vendor ID */
						errorOffset = buffer.bytesRead();
						long vendorId = buffer.readLong((byte)3);
						headerBuilder.setVendorId(vendorId);
	
						/* message type */
						errorOffset = buffer.bytesRead();
						long messageType = buffer.readLong((byte)4);
						headerBuilder.setType(messageType);

						/* message length */
						errorOffset = buffer.bytesRead();
						long mLength = buffer.readLong((byte)4);
						headerBuilder.setLength(mLength);
						
						/* message identifier */
						errorOffset = buffer.bytesRead();
						long identifier = buffer.readLong((byte)4);
						headerBuilder.setIdentifier(identifier);

					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					mHeader = (PtTlsMessageHeader)headerBuilder.toMessageHeader();
					
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
