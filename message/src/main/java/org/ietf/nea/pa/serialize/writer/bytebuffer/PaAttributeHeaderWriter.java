package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeHeaderWriter implements ImWriter<PaAttributeHeader>{

	@Override
	public void write(final PaAttributeHeader data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Attribute header cannot be NULL.");
		}

		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeHeader aHead = data;
		
		try{

			/* flags 8 bit(s) */
			Set<PaAttributeFlagsEnum> flags = aHead.getFlags();
			byte bFlags = 0;
			for (PaAttributeFlagsEnum paAttributeFlagsEnum : flags) {
				bFlags |= paAttributeFlagsEnum.bit();
			}
			buffer.writeByte(bFlags);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(aHead.getVendorId(),(byte)3);
	
			/* attribute Type 32 bit(s) */
			buffer.writeUnsignedInt(aHead.getAttributeType());
				
			/* message length 32 bit(s) */
			buffer.writeUnsignedInt(aHead.getLength());

		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
