package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageHeaderWriter implements TnccsWriter<PbMessageHeader>{

	@Override
	public void write(final PbMessageHeader data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}

		PbMessageHeader mHead = data;
		
		
		try{
			/* flags 8 bit(s) */
			Set<PbMessageFlagsEnum> flags = mHead.getFlags();
			byte bFlags = 0;
			for (PbMessageFlagsEnum pbMessageFlagsEnum : flags) {
				bFlags |= pbMessageFlagsEnum.bit();
			}
			buffer.writeByte(bFlags);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mHead.getVendorId(),(byte)3);
	
			/* message Type 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getMessageType());
	
			/* message length 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getLength());
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
