package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeErrorValueWriter implements ImWriter<PaAttributeValueError>{

	private static final byte RESERVED = 0;
	
	// TODO should be a map to make the error parameters more customizable
	private final PaAttributeErrorInformationInvalidParamValueWriter invalidParamWriter;
	private final PaAttributeErrorInformationUnsupportedVersionValueWriter unsupportedVersionWriter;
	private final PaAttributeErrorInformationUnsupportedAttributeValueWriter unsupportedAttributeWriter;
		
	PaAttributeErrorValueWriter(
			PaAttributeErrorInformationInvalidParamValueWriter invalidParamWriter,
			PaAttributeErrorInformationUnsupportedVersionValueWriter unsupportedVersionWriter,
			PaAttributeErrorInformationUnsupportedAttributeValueWriter unsupportedAttributeWriter) {
		
		this.invalidParamWriter = invalidParamWriter;
		this.unsupportedVersionWriter = unsupportedVersionWriter;
		this.unsupportedAttributeWriter = unsupportedAttributeWriter;
	}

	@Override
	public void write(final PaAttributeValueError data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueError mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* reserved 8 bit(s) */
		buffer.write(RESERVED);

		/* vendor ID 24 bit(s) */
		byte[] errVendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mValue.getErrorVendorId())
								.array(), 5, 8);
		try {
			buffer.write(errVendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Error vendor ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getErrorVendorId()));
		}

		/* error code 32 bit(s) */
		byte[] errCode = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mValue.getErrorCode()).array(), 4,
				8);
		try {
			buffer.write(errCode);
		} catch (IOException e) {
			throw new SerializationException(
					"Error code could not be written to the buffer.", e, false,
					Long.toString(mValue.getErrorCode()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
		
		/* error information */
		long errorVendor = mValue.getErrorVendorId();
		long errorCode = mValue.getErrorCode();
		
		if(errorVendor == IETFConstants.IETF_PEN_VENDORID){
			if(errorCode == PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code()){ 
				
				this.invalidParamWriter.write((PaAttributeValueErrorInformationInvalidParam)mValue.getErrorInformation(), out);
				
			}else if(errorCode == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
				
				this.unsupportedVersionWriter.write((PaAttributeValueErrorInformationUnsupportedVersion)mValue.getErrorInformation(), out);
				
			}else if(errorCode == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()){
			
				this.unsupportedAttributeWriter.write((PaAttributeValueErrorInformationUnsupportedAttribute)mValue.getErrorInformation(), out);
			
			} else {
				throw new SerializationException(
						"Error code type is not supported.",false,
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
