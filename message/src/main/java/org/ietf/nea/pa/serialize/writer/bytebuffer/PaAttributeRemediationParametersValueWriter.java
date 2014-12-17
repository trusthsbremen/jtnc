package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeRemediationParameterTypeEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

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
	public void write(final PaAttributeValueRemediationParameters data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueRemediationParameters mValue = data;

		try{
			/* reserved 8 bit(s) */
			buffer.writeByte(RESERVED);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mValue.getRpVendorId(),(byte)3);
	
			/* remediation type 32 bit(s) */
			buffer.writeUnsignedInt(mValue.getRpType());
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
		
		/* remediation parameter */
		long rpVendor = mValue.getRpVendorId();
		long rpMessageType = mValue.getRpType();
		
		if(rpVendor == IETFConstants.IETF_PEN_VENDORID){
	       	if(rpMessageType == PaAttributeRemediationParameterTypeEnum.IETF_STRING.type()){
	       		this.stringWriter.write((PaAttributeValueRemediationParameterString)mValue.getParameter(), buffer);
	       		
	       	}else if(rpMessageType == PaAttributeRemediationParameterTypeEnum.IETF_URI.type()){
	       		this.uriWriter.write((PaAttributeValueRemediationParameterUri)mValue.getParameter(), buffer);
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
