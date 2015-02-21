package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.Combined;
import de.hsbremen.tc.tnc.util.NotNull;

class PbWriter implements TnccsWriter<TnccsBatch>, Combined<TnccsWriter<PbMessageValue>> {

	private final TnccsWriter<PbBatchHeader> bHeadWriter;
	private final TnccsWriter<PbMessageHeader> mHeadWriter;
	private final Map<Long, Map<Long, TnccsWriter<PbMessageValue>>> valueWriter;
	
	PbWriter(TnccsWriter<PbBatchHeader> bHeadWriter,
			TnccsWriter<PbMessageHeader> mHeadWriter) {
		this(bHeadWriter, mHeadWriter, new HashMap<Long, Map<Long, TnccsWriter<PbMessageValue>>>());

	}
	
	
	PbWriter(TnccsWriter<PbBatchHeader> bHeadWriter,
			TnccsWriter<PbMessageHeader> mHeadWriter,
			Map<Long, Map<Long, TnccsWriter<PbMessageValue>>> valueWriter) {
		this.bHeadWriter = bHeadWriter;
		this.mHeadWriter = mHeadWriter;
		this.valueWriter = valueWriter;
	}

	@Override
	public void write(final TnccsBatch batch, final ByteBuffer buffer)
			throws SerializationException{
		NotNull.check("Batch cannot be null.", batch);
		
		NotNull.check("Output buffer cannot be null.", buffer);
		
		if(!buffer.isWriteable()){
			throw new IllegalArgumentException("Buffer must be writeable.");
		}
		
		if(!(batch instanceof PbBatch)){
			throw new IllegalArgumentException("Batch of type " + batch.getClass().getCanonicalName() + " is not supported. Bacth must be of type " +PbBatch.class.getCanonicalName()+ "." );
		}
		
		PbBatch pbBatch = (PbBatch) batch;

		/* batch header */
		
		PbBatchHeader bHead = pbBatch.getHeader();
		bHeadWriter.write(bHead, buffer);
		
		/* messages */
		List<PbMessage> msgs = pbBatch.getMessages();
		if(msgs != null && bHead.getLength() > PbMessageTlvFixedLengthEnum.BATCH.length()){
			for (PbMessage pbMessage : msgs) {
				
				PbMessageHeader mHead = pbMessage.getHeader();
				long vendor = mHead.getVendorId();
				long type = mHead.getMessageType();
				
				if(valueWriter.containsKey(vendor)){
					if(valueWriter.get(vendor).containsKey(type)){
						mHeadWriter.write(mHead, buffer);
						valueWriter.get(vendor).get(type).write(pbMessage.getValue(), buffer);
					}else{
						throw new SerializationException("Message type is not supported.",false , Long.toString(vendor), Long.toString(type));
					}
				} else {
					throw new SerializationException("Message vendor is not supported.",false, Long.toString(vendor), Long.toString(type));
				}
			}
		}
	}

	@Override
	public void add(final Long vendorId, final Long messageType,
			final TnccsWriter<PbMessageValue> reader) {
		if(valueWriter.containsKey(vendorId)){
			valueWriter.get(vendorId).put(messageType, reader);
		}else{
			valueWriter.put(vendorId, new HashMap<Long, TnccsWriter<PbMessageValue>>());
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
