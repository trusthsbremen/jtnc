package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.ietf.nea.pb.message.AbstractPbMessageValue;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializationHandler;

public class PbMessageSerializationHandler implements
		TnccsSerializationHandler<PbMessage> {

	private static final int MESSAGE_HEAD_SIZE = 
			PbMessage.FIXED_LENGTH; // in byte

	Map<Long, Map<Long, TnccsSerializationHandler<AbstractPbMessageValue>>> pbMessageTranslators;

	public PbMessageSerializationHandler(
			Map<Long, Map<Long, TnccsSerializationHandler<AbstractPbMessageValue>>> pbMessageTranslators) {
		this.pbMessageTranslators = pbMessageTranslators;
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

		if (this.pbMessageTranslators.containsKey(pbMessage.getVendorId())) {
			if (this.pbMessageTranslators.get(pbMessage.getVendorId())
					.containsKey(pbMessage.getType())) {
				encodeMessageHeader(pbMessage, out);
				this.pbMessageTranslators.get(pbMessage.getVendorId())
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
			throws SerializationException {

		PbMessageBuilderIetf builder = new PbMessageBuilderIetf();

		// ignore any given length and find out on your own.

		EnumSet<PbMessageFlagsEnum> flags;
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
			if (buffer[0] == PbMessageFlagsEnum.NOSKIP.bit()) {
				flags = EnumSet.of(PbMessageFlagsEnum.NOSKIP);
			} else {
				flags = EnumSet.noneOf(PbMessageFlagsEnum.class);
			}

			vendorId = ByteArrayHelper.toLong(new byte[] { buffer[1],
					buffer[2], buffer[3] });
			messageType = ByteArrayHelper.toLong(new byte[] { buffer[4],
					buffer[5], buffer[6], buffer[7] });
			messageLength = ByteArrayHelper.toLong(new byte[] { buffer[8], buffer[9],
					buffer[10], buffer[11] });
		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for batch is to short or stream may be closed.",
					Integer.toString(count));
		}

		if (this.pbMessageTranslators.containsKey(vendorId)) {
			if (this.pbMessageTranslators.get(vendorId)
					.containsKey(messageType)) {
				builder.setFlags(flags.toArray(new PbMessageFlagsEnum[flags
						.size()]));
				builder.setVendorId(vendorId);
				builder.setType(messageType);
				AbstractPbMessageValue value = (AbstractPbMessageValue) this.pbMessageTranslators
						.get(vendorId).get(messageType)
						.decode(in, messageLength - PbMessage.FIXED_LENGTH);
				builder.setValue(value);
				return builder.toMessage();
			}
		}
		throw new SerializationException(
				"Message vendor and/or type are not supported.",
				Long.toString(vendorId), Long.toString(messageType));
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
}
