package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributePortFilterValueWriter implements ImWriter<PaAttributeValuePortFilter>{

	private static final byte RESERVED = 0;

	@Override
	public void write(final PaAttributeValuePortFilter data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PaAttributeValuePortFilter aValue = data;

		try{
		
			List<PortFilterEntry> entries = aValue.getFilterEntries();
			if(entries != null && entries.size() > 0){
				for (PortFilterEntry entry : entries) {
					
					/* reserved (7 bit(s)) + blocking status (1 bit(s)) = 8 bit(s) */
					buffer.writeByte((byte)(RESERVED | (entry.getFilterStatus().toStatusBit() & 0x01)));
					
					/* protocol 8 bit(s) */
					buffer.writeUnsignedByte(entry.getProtocolNumber());
	
					/* port number 16 bit(s) */
					buffer.writeUnsignedShort(entry.getProtocolNumber());
		
				}
			}else{
				throw new SerializationException("No port filter entry available, but there must be at least one.", false);
			}
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
	}

}
