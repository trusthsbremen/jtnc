package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageErrorValueReader implements TnccsReader<PbMessageValueError>{

	private PbMessageValueErrorBuilder baseBuilder;
	
	// TODO should be a map to make the error parameters more customizable
	private final PbMessageErrorParameterOffsetSubValueReader offsetReader;
	private final PbMessageErrorParameterVersionSubValueReader versionReader;
	
	
	
	PbMessageErrorValueReader(PbMessageValueErrorBuilder builder,
			PbMessageErrorParameterOffsetSubValueReader offsetReader,
			PbMessageErrorParameterVersionSubValueReader versionReader) {
		this.baseBuilder = builder;
		this.offsetReader = offsetReader;
		this.versionReader = versionReader;
	}

	@Override
	public PbMessageValueError read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
	    NotNull.check("Buffer cannot be null.", buffer);
	    
		long errorOffset = 0;
		
		PbMessageValueError value = null;
		PbMessageValueErrorBuilder builder = (PbMessageValueErrorBuilder)this.baseBuilder.newInstance();

		long errorVendorId = 0L;
		int errorCode = 0;
		
		try{
			
			try{

				/* flags 8 bit(s) */
				errorOffset = buffer.bytesRead();
				builder.setErrorFlags(buffer.readByte());
				
				/* vendor ID 24 bit(s)*/
				errorOffset = buffer.bytesRead();
				errorVendorId = buffer.readLong((byte)3);
				builder.setErrorVendorId(errorVendorId);

				/* error Code 16 bit(s)*/
				errorOffset = buffer.bytesRead();
				errorCode = buffer.readInt((byte)2);
				builder.setErrorCode(errorCode);

				/* ignore Reserved 16 bit(s) */
				errorOffset = buffer.bytesRead();
				buffer.read(2);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			/* error parameter */
			// value length = overall message length - header length
			long valueLength = messageLength - this.getMinDataLength();
			
			if(errorCode == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()){
					
				PbMessageValueErrorParameterOffset param = this.offsetReader.read(buffer, valueLength);
				builder.setErrorParameter(param);
					
			}else if(errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
					
				PbMessageValueErrorParameterVersion param = this.versionReader.read(buffer, valueLength);
				builder.setErrorParameter(param);
				
			}else if (errorCode != PbMessageErrorCodeEnum.IETF_LOCAL.code() && errorCode != PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code()){
				try{
					// skip the remaining bytes of the message
					buffer.read(valueLength);
				}catch (BufferUnderflowException e){
					throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
				}
				// TODO make a default error object?
				return null;
			}

			value = (PbMessageValueError)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.ERR_VALUE.length();
	}

}
