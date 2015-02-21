package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersionBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageErrorParameterVersionSubValueReader implements TnccsReader<PbMessageValueErrorParameterVersion>{

	private  PbMessageValueErrorParameterVersionBuilder baseBuilder;
	
	PbMessageErrorParameterVersionSubValueReader(PbMessageValueErrorParameterVersionBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public  PbMessageValueErrorParameterVersion read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueErrorParameterVersion value = null;
		PbMessageValueErrorParameterVersionBuilder builder = (PbMessageValueErrorParameterVersionBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* bad version 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				short badVersion = buffer.readShort((byte)1);
				builder.setBadVersion(badVersion);
				
				/* max version 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				short maxVersion = buffer.readShort((byte)1);
				builder.setMaxVersion(maxVersion);
				
				/* min version 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				short minVersion = buffer.readShort((byte)1);
				builder.setMinVersion(minVersion);
				
				/* ignore reserved 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				buffer.readByte();
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueErrorParameterVersion)builder.toObject();
			
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
