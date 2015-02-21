package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParametersValueWriter implements TnccsWriter<PbMessageValueRemediationParameters>{

	private static final byte RESERVED = 0;
	
	// TODO should be a map to make the remediation parameters more customizable
	private final PbMessageRemediationParameterStringSubValueWriter stringWriter;
	private final PbMessageRemediationParameterUriSubValueWriter uriWriter;
		
	public PbMessageRemediationParametersValueWriter(PbMessageRemediationParameterStringSubValueWriter stringWriter, PbMessageRemediationParameterUriSubValueWriter uriWriter ){
		this.stringWriter = stringWriter;
		this.uriWriter = uriWriter;
	}
		
	@Override
	public void write(final PbMessageValueRemediationParameters data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueRemediationParameters mValue = data;
	
		try{
			/* reserved 8 bit(s) */
			buffer.writeByte(RESERVED);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mValue.getRpVendorId(), (byte)3);
	
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
	       	if(rpMessageType == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
	       		this.stringWriter.write((PbMessageValueRemediationParameterString)mValue.getParameter(), buffer);
	       		
	       	}else if(rpMessageType == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
	       		this.uriWriter.write((PbMessageValueRemediationParameterUri)mValue.getParameter(), buffer);
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
