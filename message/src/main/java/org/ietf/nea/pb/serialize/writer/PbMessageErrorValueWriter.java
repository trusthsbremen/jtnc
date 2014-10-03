package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

class PbMessageErrorValueWriter implements TnccsWriter<PbMessageValueError>{

	private static final short RESERVED = 0;
	
	@Override
	public void write(final PbMessageValueError data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PbMessageValueError mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageErrorFlagsEnum> flags = mValue.getErrorFlags();
		byte bFlags = 0;
		for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
			bFlags |= pbMessageErrorFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mValue.getErrorVendorId())
								.array(), 5, 7);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Error vendor ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getErrorVendorId()));
		}

		/* error Code */
		byte[] code = ByteBuffer.allocate(2).putShort(mValue.getErrorCode()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Error code could not be written to the buffer.", e, false,
					Short.toString(mValue.getErrorCode()));
		}
		
		/* reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(RESERVED).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e, false,
					Short.toString(RESERVED));
		}
		
		try {
			buffer.writeTo(out);
			
			/* error parameter */
			out.write((mValue.getErrorParameter() != null)? mValue.getErrorParameter() : new byte[0]); 
			
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
