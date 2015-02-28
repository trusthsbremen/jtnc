package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueErrorBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeErrorValueReader implements ImReader<PaAttributeValueError>{

	private PaAttributeValueErrorBuilder baseBuilder;
	
	// TODO should be a map to make the error parameters more customizable
	private final PaAttributeErrorInformationInvalidParamValueReader invalidParamReader;
	private final PaAttributeErrorInformationUnsupportedVersionValueReader unsupportedVersionReader;
	private final PaAttributeErrorInformationUnsupportedAttributeValueReader unsupportedAttributeReader;
	
	public PaAttributeErrorValueReader(PaAttributeValueErrorBuilder builder, PaAttributeErrorInformationInvalidParamValueReader invalidParamReader, PaAttributeErrorInformationUnsupportedVersionValueReader unsupportedVersionReader, PaAttributeErrorInformationUnsupportedAttributeValueReader unsupportedAttributeReader){
		this.baseBuilder = builder;
		this.invalidParamReader = invalidParamReader;
		this.unsupportedAttributeReader = unsupportedAttributeReader;
		this.unsupportedVersionReader = unsupportedVersionReader;
	}
	
	@Override
	public PaAttributeValueError read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueError value = null;
		PaAttributeValueErrorBuilder builder = (PaAttributeValueErrorBuilder)this.baseBuilder.newInstance();

		long errorVendorId = 0L;
		long errorCode = 0L;
		
		try{
			
			try{
				
				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.readByte();
				
				/* vendor ID */
				errorOffset = buffer.bytesRead();
				errorVendorId = buffer.readLong((byte)3);
				builder.setErrorVendorId(errorVendorId);
				
				/* code */
				errorOffset = buffer.bytesRead();
				errorCode = buffer.readLong((byte)4);
				builder.setErrorCode(errorCode);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			/* error parameter */
			// value length = header length - overall message length
			long valueLength = messageLength - PaAttributeTlvFixedLengthEnum.ERR_INF.length();

			try{
				if(errorCode == PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code()){ 
					
					PaAttributeValueErrorInformationInvalidParam errorInformation = this.invalidParamReader.read(buffer, valueLength);
					builder.setErrorInformation(errorInformation);
					
				}else if(errorCode == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
					
					PaAttributeValueErrorInformationUnsupportedVersion errorInformation = this.unsupportedVersionReader.read(buffer, valueLength);
					builder.setErrorInformation(errorInformation);
					
				}else if(errorCode == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()){
				
					PaAttributeValueErrorInformationUnsupportedAttribute errorInformation = this.unsupportedAttributeReader.read(buffer, valueLength);
					builder.setErrorInformation(errorInformation);
				
				}else{
					try{
						// skip the remaining bytes of the message
						buffer.read(valueLength);
					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}
					// TODO make a default error object?
					return null;
				}
			}catch(ValidationException e){
				// catch exception and add throw with recalculated offset, pass on the rule exception
				throw new ValidationException(e.getMessage(), e.getCause(), e.getExceptionOffset() + errorOffset);
			}

			value = (PaAttributeValueError)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.ERR_INF.length();
	}

}
