package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PbMessageErrorValueReader implements TnccsReader<PbMessageValueError>{

	private PbMessageValueErrorBuilder builder;
	
	// TODO should be a map to make the error parameters more customizable
	private final PbMessageErrorParameterOffsetSubValueReader offsetReader;
	private final PbMessageErrorParameterVersionSubValueReader versionReader;
	
	
	
	PbMessageErrorValueReader(PbMessageValueErrorBuilder builder,
			PbMessageErrorParameterOffsetSubValueReader offsetReader,
			PbMessageErrorParameterVersionSubValueReader versionReader) {
		this.builder = builder;
		this.offsetReader = offsetReader;
		this.versionReader = versionReader;
	}

	@Override
	public PbMessageValueError read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PbMessageValueError value = null;
		builder = (PbMessageValueErrorBuilder)builder.clear();

		long errorVendorId = 0L;
		short errorCode = 0;
		
		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* flags */
				byteSize = 1;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				this.builder.setErrorFlags(buffer[0]);
				errorOffset += byteSize;
				
				/* vendor ID */
				byteSize = 3;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				errorVendorId = ByteArrayHelper.toLong(buffer);
				this.builder.setErrorVendorId(errorVendorId);
				errorOffset += byteSize;
				
				/* error Code */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				errorCode = ByteArrayHelper.toShort(buffer);
				this.builder.setErrorCode(errorCode);
				errorOffset += byteSize;
				
				/* ignore Reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
			
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true);
			}

			/* error parameter */
			// value length = header length - overall message length
			long valueLength = messageLength - errorOffset;

			try{
				if(errorCode == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code() || errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()){
					
					PbMessageValueErrorParameterOffset param = this.offsetReader.read(in, valueLength);
					this.builder.setErrorParameter(param);
					
				}else if(errorCode == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
					
					PbMessageValueErrorParameterVersion param = this.versionReader.read(in, valueLength);
					this.builder.setErrorParameter(param);
				
				}else if (errorCode != PbMessageErrorCodeEnum.IETF_LOCAL.code() && errorCode != PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code()){
					try{
						// skip the remaining bytes of the message
						in.skip(valueLength);
					}catch (IOException e1){
						throw new SerializationException("Bytes from InputStream could not be skipped, stream seems closed.", true);
					}
					return null;
				}
			}catch(ValidationException e){
				// catch exception and add throw with recalculated offset, pass on the rule exception
				throw new ValidationException(e.getMessage(), (RuleException)e.getCause(),e.getExceptionOffset() + errorOffset);
			}
			
			value = (PbMessageValueError)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLength.ERR_VALUE.length();
	}

}
