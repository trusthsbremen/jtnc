package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageErrorValueWriter implements TnccsWriter<PbMessageValueError>{

	private static final short RESERVED = 0;
	
	// TODO should be a map to make the error parameters more customizable
	private final PbMessageErrorParameterOffsetSubValueWriter offsetWriter;
	private final PbMessageErrorParameterVersionSubValueWriter versionWriter;

	PbMessageErrorValueWriter(
			PbMessageErrorParameterOffsetSubValueWriter offsetWriter,
			PbMessageErrorParameterVersionSubValueWriter versionWriter) {
		this.offsetWriter = offsetWriter;
		this.versionWriter = versionWriter;
	}

	@Override
	public void write(final PbMessageValueError data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueError mValue = data;
		
		try{

			/* flags 8 bit(s) */
			Set<PbMessageErrorFlagsEnum> flags = mValue.getErrorFlags();
			byte bFlags = 0;
			for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
				bFlags |= pbMessageErrorFlagsEnum.bit();
			}
			buffer.writeByte(bFlags);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mValue.getErrorVendorId(), (byte)3);
	
			/* error Code */
			buffer.writeUnsignedShort(mValue.getErrorCode());
			
			/* reserved */
			buffer.writeShort(RESERVED);
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
		/* error parameter */
		long errorVendor = mValue.getErrorVendorId();
		long errorCode = mValue.getErrorCode();
		
		if(errorVendor == IETFConstants.IETF_PEN_VENDORID){
	       	if(errorCode == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()){
	       		
	       		this.offsetWriter.write((PbMessageValueErrorParameterOffset)mValue.getErrorParameter(), buffer);
	       		
	       	}else if(errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
	       		
	       		this.versionWriter.write((PbMessageValueErrorParameterVersion)mValue.getErrorParameter(), buffer);
	       	
	       	}else if(errorCode != PbMessageErrorCodeEnum.IETF_LOCAL.code() && errorCode != PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code()){
				
	       		throw new SerializationException(
						"Error message type is not supported.",false,
						Long.toString(errorVendor),
						Long.toString(errorCode));
			}
		} else {
			throw new SerializationException(
					"Error vendor ID is not supported.",false,
					Long.toString(errorVendor),
					Long.toString(errorCode));
		}
	}

}
