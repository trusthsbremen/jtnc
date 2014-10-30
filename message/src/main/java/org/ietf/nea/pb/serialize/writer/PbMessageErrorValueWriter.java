package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

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
	public void write(final PbMessageValueError data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueError mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageErrorFlagsEnum> flags = mValue.getErrorFlags();
		byte bFlags = 0;
		for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
			bFlags |= pbMessageErrorFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mValue.getErrorVendorId())
								.array(), 5, 7);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Error vendor ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getErrorVendorId()));
		}

		/* error Code */
		byte[] code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(mValue.getErrorCode()).array(),2,4);
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Error code could not be written to the buffer.", e, false,
					Integer.toString(mValue.getErrorCode()));
		}
		
		/* reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(RESERVED).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e, false,
					Short.toString(RESERVED));
		}
		
		try {
			buffer.writeTo(out);
			
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
		
		/* error parameter */
		long errorVendor = mValue.getErrorVendorId();
		long errorCode = mValue.getErrorCode();
		
		if(errorVendor == IETFConstants.IETF_PEN_VENDORID){
	       	if(errorCode == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()){
	       		
	       		this.offsetWriter.write((PbMessageValueErrorParameterOffset)mValue.getErrorParameter(), out);
	       		
	       	}else if(errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
	       		
	       		this.versionWriter.write((PbMessageValueErrorParameterVersion)mValue.getErrorParameter(), out);
	       	
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
