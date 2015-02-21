package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeErrorInformationUnsupportedAttributeValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedAttribute>{
	

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedAttribute data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PaAttributeValueErrorInformationUnsupportedAttribute aValue = data;
		
		try{
			/* message header copy 64 bit(s) */
			/* copy version 8 bit(s) */
			buffer.writeUnsignedByte(data.getMessageHeader().getVersion());
	
			/* copy reserved 24 bit(s) */
			buffer.write(data.getMessageHeader().getReserved());
			
			/* copy identifier 32 bit(s) */
			buffer.writeUnsignedInt(data.getMessageHeader().getIdentifier());
			
			/* attribute header copy 64 bit(s) */
			PaAttributeHeader aHead = aValue.getAttributeHeader();
			
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
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
	}

}
