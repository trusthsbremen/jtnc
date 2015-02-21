package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeErrorInformationInvalidParamValueWriter implements ImWriter<PaAttributeValueErrorInformationInvalidParam>{

	@Override
	public void write(final PaAttributeValueErrorInformationInvalidParam data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PaAttributeValueErrorInformationInvalidParam aValue = data;
		try{
			/* message header copy 64 bit(s) */
			/* copy version 8 bit(s) */
			buffer.writeUnsignedByte(data.getMessageHeader().getVersion());
	
			/* copy reserved 24 bit(s) */
			buffer.write(data.getMessageHeader().getReserved());
			
			/* copy identifier 32 bit(s) */
			buffer.writeUnsignedInt(data.getMessageHeader().getIdentifier());
			
			/* offset 32 bit(s) */
			buffer.writeUnsignedInt(aValue.getOffset());

		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
	}

}
