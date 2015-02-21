package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffsetBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageErrorParameterOffsetSubValueReader implements TnccsReader<PbMessageValueErrorParameterOffset>{

	private  PbMessageValueErrorParameterOffsetBuilder baseBuilder;
	
	PbMessageErrorParameterOffsetSubValueReader(PbMessageValueErrorParameterOffsetBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public  PbMessageValueErrorParameterOffset read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueErrorParameterOffset value = null;
		PbMessageValueErrorParameterOffsetBuilder builder = (PbMessageValueErrorParameterOffsetBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				/* offset 32 bit(s)*/
				errorOffset = buffer.bytesRead();
				long offset = buffer.readLong((byte)4);
				builder.setOffset(offset);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueErrorParameterOffset)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {

		return PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length(); 
	}
}
