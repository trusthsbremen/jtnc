package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeErrorInformationUnsupportedVersionValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedVersion>{

	private static final byte[] RESERVED = new byte[]{0,0};

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedVersion data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationUnsupportedVersion aValue = data;

		try{
			/* message header copy 64 bit(s) */
			/* copy version 8 bit(s) */
			buffer.writeUnsignedByte(data.getMessageHeader().getVersion());
	
			/* copy reserved 24 bit(s) */
			buffer.write(data.getMessageHeader().getReserved());
			
			/* copy identifier 32 bit(s) */
			buffer.writeUnsignedInt(data.getMessageHeader().getIdentifier());
			
			/* max version 8 bit(s) */
			buffer.writeUnsignedByte(aValue.getMaxVersion());
			
			/* min version 8 bit(s) */
			buffer.writeUnsignedByte(aValue.getMinVersion());
			
			/* Reserved 16 bit(s) */
			buffer.write(RESERVED);

		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
	}

}
