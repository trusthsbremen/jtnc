package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ietf.nea.pb.exception.PbMessageUnknownException;
import org.ietf.nea.pb.message.AbstractPbMessageValue;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageBuilder;
import org.ietf.nea.pb.message.PbMessageValueBuilderHsb;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;
import de.hsbremen.tc.tnc.util.Combined;

public class PbMessageSerializer implements TnccsSerializer<PbMessage>, Combined<TnccsSerializer<AbstractPbMessageValue>>
		/*TnccsSerializer<PbMessage>*/ {

	private static final int MESSAGE_HEAD_SIZE = 
			PbMessage.FIXED_LENGTH; // in byte

	private PbMessageBuilder pbBuilder;
	
	private Map<Long, Map<Long, TnccsSerializer<AbstractPbMessageValue>>> pbMessageValueSerializers;

	public PbMessageSerializer(PbMessageBuilder builder){
		this.pbBuilder = builder;
		this.pbMessageValueSerializers = new HashMap<>();
		
	}

	/* Encode/Decode Methods */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hsbremen.jtnc.nar.message.visitor.MessageVisitor#visit(de.hsbremen
	 * .jtnc.message.TnccsBatch)
	 */
	@Override
	public void encode(final PbMessage pbMessage, final OutputStream out)
			throws SerializationException {

		if (this.pbMessageValueSerializers.containsKey(pbMessage.getVendorId())) {
			if (this.pbMessageValueSerializers.get(pbMessage.getVendorId())
					.containsKey(pbMessage.getType())) {
				encodeMessageHeader(pbMessage, out);
				this.pbMessageValueSerializers.get(pbMessage.getVendorId())
						.get(pbMessage.getType())
						.encode(pbMessage.getValue(), out);
			} else {
				throw new SerializationException(
						"Message type is not supported.",
						Long.toString(pbMessage.getVendorId()),
						Long.toString(pbMessage.getType()));
			}
		} else {
			throw new SerializationException(
					"Message vendor is not supported.", Long.toString(pbMessage
							.getVendorId()), Long.toString(pbMessage.getType()));
		}
	}

	@Override
	public PbMessage decode(final InputStream in, final long length)
			throws SerializationException, ValidationException{

		pbBuilder = (PbMessageBuilder) pbBuilder.clear();

		// ignore any given length and find out on your own.

		byte flags = 0;
		long vendorId = 0;
		long messageType = 0;
		long messageLength = 0;

		byte[] buffer = new byte[MESSAGE_HEAD_SIZE];
		int count = 0;
		// wait till data is available
		while (count == 0) {
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
		}

		if (count >= MESSAGE_HEAD_SIZE) {
			flags = buffer[0];
			pbBuilder.setFlags(flags);

			vendorId = ByteArrayHelper.toLong(new byte[] { buffer[1],
					buffer[2], buffer[3] });
			pbBuilder.setVendorId(vendorId);
			
			messageType = ByteArrayHelper.toLong(new byte[] { buffer[4],
					buffer[5], buffer[6], buffer[7] });
			pbBuilder.setType(messageType);
			
			messageLength = ByteArrayHelper.toLong(new byte[] { buffer[8], buffer[9],
					buffer[10], buffer[11] });
			
		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for batch is to short or stream may be closed.",
					Integer.toString(count));
		}

		PbMessage message = null;
		if (this.pbMessageValueSerializers.containsKey(vendorId)) {
			if (this.pbMessageValueSerializers.get(vendorId)
					.containsKey(messageType)) {
				
				AbstractPbMessageValue value = (AbstractPbMessageValue) this.pbMessageValueSerializers
						.get(vendorId).get(messageType)
						.decode(in, messageLength - PbMessage.FIXED_LENGTH);
				pbBuilder.setValue(value);

				message = (PbMessage) pbBuilder.toMessage();
				
				return message;
			}
		}
		
		// Unknown message error creation
		byte[] dump = dumpContent(in, messageLength - PbMessage.FIXED_LENGTH);
		pbBuilder.setValue(PbMessageValueBuilderHsb.createUnknownValue(((flags & PbMessageFlagsEnum.NOSKIP.bit()) > 0), dump));
		message = (PbMessage) pbBuilder.toMessage();
		
		throw new SerializationException("Message with vendor ID " + vendorId + " and type " + messageType + " is unknown.",
				new PbMessageUnknownException("Message with vendor ID " + vendorId + " and type " + messageType + " is unknown.",message), Long.toString(vendorId), Long.toString(messageType));
	}

	private byte[] dumpContent(final InputStream in, long length) throws SerializationException{
		byte[] buffer = new byte[0];
		long contentLength = length;
		int count = 0;
		byte[] temp = new byte[0];
		
		for(; contentLength > 0; contentLength -= count){
			
			buffer = (contentLength < 65535) ? new byte[(int)contentLength] : new byte[65535];
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
			
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, count));

		}
		if(temp != null && temp.length > 0){
			return temp;
		}else{
			return new byte[0];
		}
		
	}
	private void encodeMessageHeader(final PbMessage pbMessage, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Flags 8 bit(s) */
		Set<PbMessageFlagsEnum> flags = pbMessage.getFlags();
		byte bFlags = 0;
		for (PbMessageFlagsEnum pbMessageFlagsEnum : flags) {
			bFlags |= pbMessageFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* Vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(pbMessage.getVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e,
					Long.toString(pbMessage.getVendorId()));
		}

		/* Message Type 32 bit(s) */
		byte[] messageType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(pbMessage.getType()).array(), 4,
				8);
		try {
			buffer.write(messageType);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e,
					Long.toString(pbMessage.getType()));
		}

		/* Message length 32 bit(s) only reserved here and later added */
		byte[] length = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(pbMessage.getLength()).array(),
				4, 8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e,
					Long.toString(pbMessage.getLength()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException(
					"Batch header could not be written to the OutputStream.", e);
		}

	}
	
	@Override
	public void add(final Long vendorId, final Long messageType,
			final TnccsSerializer<AbstractPbMessageValue> validator) {
		if(pbMessageValueSerializers.containsKey(vendorId)){
			pbMessageValueSerializers.get(vendorId).put(messageType, validator);
		}else{
			pbMessageValueSerializers.put(vendorId, new HashMap<Long, TnccsSerializer<AbstractPbMessageValue>>());
			pbMessageValueSerializers.get(vendorId).put(messageType, validator);
		}
		
	}

	@Override
	public void remove(final Long vendorId, final Long messageType) {
		if(pbMessageValueSerializers.containsKey(vendorId)){
			if(pbMessageValueSerializers.get(vendorId).containsKey(messageType)){
				pbMessageValueSerializers.get(vendorId).remove(messageType);
			}
			if(pbMessageValueSerializers.get(vendorId).isEmpty()){
				pbMessageValueSerializers.remove(vendorId);
			}
		}
	}
}
