package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeErrorInformationUnsupportedVersionValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedVersion>{

	private static final byte[] RESERVED = new byte[]{0,0};
	
	private PaMessageHeaderWriter writer;
	
	PaAttributeErrorInformationUnsupportedVersionValueWriter(
			PaMessageHeaderWriter writer) {
		this.writer = writer;
	}

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedVersion data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationUnsupportedVersion aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* message header copy 64 bit(s) */
		this.writer.write(aValue.getMessageHeader(), out);
		
		/* max version 8 bit(s) */
		buffer.write(aValue.getMaxVersion());
		
		/* min version 8 bit(s) */
		buffer.write(aValue.getMinVersion());
		
		/* Reserved 16 bit(s) */
		try {
			buffer.write(RESERVED);
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
