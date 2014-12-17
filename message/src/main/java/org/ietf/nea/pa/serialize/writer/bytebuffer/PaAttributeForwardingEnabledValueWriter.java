package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabled;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeForwardingEnabledValueWriter implements ImWriter<PaAttributeValueForwardingEnabled>{

	@Override
	public void write(final PaAttributeValueForwardingEnabled data, final ByteBuffer buffer)
			throws SerializationException {
		
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueForwardingEnabled aValue = data;
		
		try{
			/* forwarding status */
			buffer.writeUnsignedInt(aValue.getForwardingStatus().status());

		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}
}
