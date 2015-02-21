package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.util.HashMap;
import java.util.Map;

import org.ietf.nea.pt.message.PtTlsMessage;
import org.ietf.nea.pt.message.PtTlsMessageHeader;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.value.PtTlsMessageValue;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.Combined;
import de.hsbremen.tc.tnc.util.NotNull;

class PtTlsWriter implements TransportWriter<TransportMessage>, Combined<TransportWriter<PtTlsMessageValue>> {

	private final TransportWriter<PtTlsMessageHeader> mHeadWriter;
	private final Map<Long, Map<Long, TransportWriter<PtTlsMessageValue>>> valueWriter;
	
	PtTlsWriter(TransportWriter<PtTlsMessageHeader> mHeadWriter) {
		this(mHeadWriter, new HashMap<Long, Map<Long, TransportWriter<PtTlsMessageValue>>>());

	}
	
	
	PtTlsWriter(TransportWriter<PtTlsMessageHeader> mHeadWriter,
			Map<Long,Map<Long, TransportWriter<PtTlsMessageValue>>> valueWriter) {
		this.mHeadWriter = mHeadWriter;
		this.valueWriter = valueWriter;
	}

	@Override
	public void write(final TransportMessage m, final ByteBuffer buffer)
			throws SerializationException{
		NotNull.check("Message cannot be null.", m);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		if(!buffer.isWriteable()){
			throw new IllegalArgumentException("Buffer must be writeable.");
		}
		
		if(!( m instanceof PtTlsMessage)){
			throw new IllegalArgumentException("Message of type " + m.getClass().getCanonicalName() + " is not supported. Message must be of type " +PtTlsMessage.class.getCanonicalName()+ "." );
		}
		
		
		PtTlsMessage message =  (PtTlsMessage) m;

		/* message header */
		
		PtTlsMessageHeader mHead = message.getHeader();
		mHeadWriter.write(mHead, buffer);
		
		/* messages */
		PtTlsMessageValue value = message.getValue();
		
		if(value != null && mHead.getLength() > PtTlsMessageTlvFixedLengthEnum.MESSAGE.length()){

			long vendor = mHead.getVendorId();
			long type = mHead.getMessageType();
					
			if(valueWriter.containsKey(vendor)){
				if(valueWriter.get(vendor).containsKey(type)){
					valueWriter.get(vendor).get(type).write(message.getValue(), buffer);
				}else{
					throw new SerializationException("Message type is not supported.",false , Long.toString(vendor), Long.toString(type));
				}
			} else {
				throw new SerializationException("Message vendor is not supported.",false, Long.toString(vendor), Long.toString(type));
			}
		}
	
	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final TransportWriter<PtTlsMessageValue> reader) {
		if(valueWriter.containsKey(vendorId)){
			valueWriter.get(vendorId).put(messageType, reader);
		}else{
			valueWriter.put(vendorId, new HashMap<Long, TransportWriter<PtTlsMessageValue>>());
			valueWriter.get(vendorId).put(messageType, reader);
		}
		
	}

	@Override
	public void remove(final Long vendorId, final Long messageType) {
		if(valueWriter.containsKey(vendorId)){
			if(valueWriter.get(vendorId).containsKey(messageType)){
				valueWriter.get(vendorId).remove(messageType);
			}
			if(valueWriter.get(vendorId).isEmpty()){
				valueWriter.remove(vendorId);
			}
		}
	}

}
