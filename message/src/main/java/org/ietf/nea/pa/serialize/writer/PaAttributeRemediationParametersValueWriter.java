package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeRemediationParametersValueWriter implements ImWriter<PaAttributeValueRemediationParameters>{

	private static final byte RESERVED = 0;
	
	// TODO should be a map to make the remediation parameters more customizable
	private final PaAttributeRemediationParameterStringValueWriter stringWriter;
	private final PaAttributeRemediationParameterUriValueWriter uriWriter;
		
	PaAttributeRemediationParametersValueWriter(PaAttributeRemediationParameterStringValueWriter stringWriter, PaAttributeRemediationParameterUriValueWriter uriWriter ){
		this.stringWriter = stringWriter;
		this.uriWriter = uriWriter;
	}
		
	@Override
	public void write(final PaAttributeValueRemediationParameters data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueRemediationParameters mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* reserved 8 bit(s) */
		buffer.write(RESERVED);

		/* vendor ID 24 bit(s) */
		byte[] rpVendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mValue.getRpVendorId())
								.array(), 5, 8);
		try {
			buffer.write(rpVendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Remediation vendor ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getRpVendorId()));
		}

		/* remediation type 32 bit(s) */
		byte[] rpType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mValue.getRpType()).array(), 4,
				8);
		try {
			buffer.write(rpType);
		} catch (IOException e) {
			throw new SerializationException(
					"Remediation type could not be written to the buffer.", e, false,
					Long.toString(mValue.getRpType()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
		
		/* remediation parameter */
		long rpVendor = mValue.getRpVendorId();
		long rpMessageType = mValue.getRpType();
		
		if(rpVendor == IETFConstants.IETF_PEN_VENDORID){
	       	if(rpMessageType == PaAttributeRemediationParameterTypeEnum.IETF_STRING.type()){
	       		this.stringWriter.write((PaAttributeValueRemediationParameterString)mValue.getParameter(), out);
	       		
	       	}else if(rpMessageType == PaAttributeRemediationParameterTypeEnum.IETF_URI.type()){
	       		this.uriWriter.write((PaAttributeValueRemediationParameterUri)mValue.getParameter(), out);
	       	} else {
				throw new SerializationException(
						"Remediation message type is not supported.",false,
						Long.toString(rpVendor),
						Long.toString(rpMessageType));
			}
		} else {
			throw new SerializationException(
					"Remediation vendor ID is not supported.",false,
					Long.toString(rpVendor),
					Long.toString(rpMessageType));
		}
	}

}
