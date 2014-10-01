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
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;
import de.hsbremen.tc.tnc.util.Combined;

class PbMessageSerializer implements TnccsSerializer<PbMessage>, Combined<TnccsSerializer<AbstractPbMessageValue>>
		/*TnccsSerializer<PbMessage>*/ {

	private static final int MESSAGE_HEAD_FIXED_SIZE = PbMessageTlvFixedLength.MESSAGE.length(); // in byte

	private PbMessageBuilder pbBuilder;
	
	private Map<Long, Map<Long, TnccsSerializer<AbstractPbMessageValue>>> pbMessageValueSerializers;

	PbMessageSerializer(PbMessageBuilder builder){
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
						"Message type is not supported.",false ,0 ,
						Long.toString(pbMessage.getVendorId()),
						Long.toString(pbMessage.getType()));
			}
		} else {
			throw new SerializationException(
					"Message vendor is not supported.",false, 0, Long.toString(pbMessage
							.getVendorId()), Long.toString(pbMessage.getType()));
		}
	}

	@Override
	public PbMessage decode(final InputStream in, final long length)
			throws SerializationException, ValidationException {

		pbBuilder = (PbMessageBuilder) pbBuilder.clear();

		// ignore any given length and find out on your own.
		long messageLength = 0L;
		
		byte flags = 0;
		long vendorId = 0L;
		long messageType = 0L;

		byte[] buffer = new byte[0];
		try{
			/* flags */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			flags = buffer[0];
			pbBuilder.setFlags(flags);

			/* vendor ID */
			buffer = ByteArrayHelper.arrayFromStream(in, 3);
			vendorId = ByteArrayHelper.toLong(buffer);
			pbBuilder.setVendorId(vendorId);
			
			/* message type */ 
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			messageType = ByteArrayHelper.toLong(buffer);
			pbBuilder.setType(messageType);
			
			/* message length */
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			messageLength = ByteArrayHelper.toLong(buffer);
			
		}catch (IOException e){
			throw new SerializationException("Returned data for message header is to short or stream may be closed.",true,0,Integer.toString(buffer.length));
		}

		PbMessage message = null;
		if (this.pbMessageValueSerializers.containsKey(vendorId)) {
			if (this.pbMessageValueSerializers.get(vendorId)
					.containsKey(messageType)) {
				
				AbstractPbMessageValue value = (AbstractPbMessageValue) this.pbMessageValueSerializers
						.get(vendorId).get(messageType)
						.decode(in, messageLength - MESSAGE_HEAD_FIXED_SIZE);
				pbBuilder.setValue(value);

				message = (PbMessage) pbBuilder.toMessage();
				
				return message;
			}
		}
		
		/* Unknown message error creation */
		byte[] dump = dumpContent(in, messageLength - MESSAGE_HEAD_FIXED_SIZE);
		pbBuilder.setValue(PbMessageValueBuilderHsb.createUnknownValue(((flags & PbMessageFlagsEnum.NOSKIP.bit()) > 0), dump));
		message = (PbMessage) pbBuilder.toMessage();
	
		throw new SerializationException("Message with vendor ID " + vendorId + " and type " + messageType + " is unknown.",
				new PbMessageUnknownException("Message with vendor ID " + vendorId + " and type " + messageType + " is unknown.",message) ,false ,0 , Long.toString(vendorId), Long.toString(messageType));
	}

	private byte[] dumpContent(final InputStream in, long length) throws SerializationException{
		
		long contentLength = length;
		byte[] buffer = new byte[0];
		byte[] temp = new byte[0];
		
		for(; contentLength > 0; contentLength -= buffer.length){

			try {
				buffer = ByteArrayHelper.arrayFromStream(in, ((contentLength < 65535) ? (int)contentLength : 65535));
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e, true, 0);
			}
			
			temp = ByteArrayHelper.mergeArrays(temp, Arrays.copyOfRange(buffer,0, buffer.length));

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

		/* flags 8 bit(s) */
		Set<PbMessageFlagsEnum> flags = pbMessage.getFlags();
		byte bFlags = 0;
		for (PbMessageFlagsEnum pbMessageFlagsEnum : flags) {
			bFlags |= pbMessageFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(pbMessage.getVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false, 0,
					Long.toString(pbMessage.getVendorId()));
		}

		/* message Type 32 bit(s) */
		byte[] messageType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(pbMessage.getType()).array(), 4,
				8);
		try {
			buffer.write(messageType);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e, false, 0,
					Long.toString(pbMessage.getType()));
		}

		/* message length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(pbMessage.getLength()).array(),
				4, 8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e, false, 0,
					Long.toString(pbMessage.getLength()));
		}

		// write to output
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException(
					"Batch header could not be written to the OutputStream.", e, true, 0);
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
