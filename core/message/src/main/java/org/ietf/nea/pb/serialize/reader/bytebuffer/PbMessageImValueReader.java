package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueImBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageImValueReader implements TnccsReader<PbMessageValueIm>{

	private PbMessageValueImBuilder baseBuilder;
	
	PbMessageImValueReader(PbMessageValueImBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueIm read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
	    
	    NotNull.check("Buffer cannot be null.", buffer);
		
	    long errorOffset = 0;
		
		PbMessageValueIm value = null;
		PbMessageValueImBuilder builder = (PbMessageValueImBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* flags 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				builder.setImFlags(buffer.readByte());
			
				/* sub vendor ID 24 bit(s)*/ 
				errorOffset = buffer.bytesRead();
				long subVendorId = buffer.readLong((byte)3);
				builder.setSubVendorId(subVendorId);

				/* sub message type 32 bit(s) */
				errorOffset = buffer.bytesRead();
				long subType = buffer.readLong((byte)4);
				builder.setSubType(subType);
				
				/* collector ID 16 bit(s)*/
				errorOffset = buffer.bytesRead();
				long collectorId = buffer.readLong((byte)2);
				builder.setCollectorId(collectorId);

				
				/* validator ID 16 bit(s) */
				errorOffset = buffer.bytesRead();
				long validatorId = buffer.readLong((byte)2);
				builder.setValidatorId(validatorId);	
			
				/* PA message */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle an array of Integer.MAX_VALUE 
				// the length is shortened here and my switch to a negative value.
				byte[] imMessage = buffer.read((int)(messageLength - this.getMinDataLength()));
				builder.setMessage(imMessage);

			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueIm)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.IM_VALUE.length();
	}

}
