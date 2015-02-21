package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageImValueWriter implements TnccsWriter<PbMessageValueIm>{

	@Override
	public void write(final PbMessageValueIm data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueIm mValue = data;
		
		
		try{
			/* flags 8 bit(s) */
			Set<PbMessageImFlagsEnum> flags = mValue.getImFlags();
			byte bFlags = 0;
			for (PbMessageImFlagsEnum pbMessageImFlagsEnum : flags) {
				bFlags |= pbMessageImFlagsEnum.bit();
			}
			buffer.writeByte(bFlags);
	
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mValue.getSubVendorId(),(byte)3);
	
			/* message type 32 bit(s) */
			buffer.writeUnsignedInt(mValue.getSubType());
	
			/* collector ID */
			buffer.writeDigits(mValue.getCollectorId(), (byte)2);
			
			
			/* validator ID */
			buffer.writeDigits(mValue.getValidatorId(),(byte)2);
	
				
			/* im message */
			buffer.write((mValue.getMessage() != null)? mValue.getMessage() : new byte[0]); 
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
