package org.ietf.nea.pb.serialize.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.util.Combined;

class PbWriter implements TnccsWriter<PbBatch>, Combined<TnccsWriter<PbMessageValue>> {

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
	public void write(final PbBatch batch, final OutputStream out)
			throws SerializationException{
		if(batch == null){
			throw new NullPointerException("Batch cannot be null.");
		}
		if(out == null){
			throw new NullPointerException("OutputStream cannot be null.");
		}
		
		BufferedOutputStream bOut = (out instanceof BufferedOutputStream)? (BufferedOutputStream)out: new BufferedOutputStream(out);

		/* batch header */
		
		PbBatchHeader bHead = batch.getHeader();
		bHeadWriter.write(bHead, bOut);
		
		/* messages */
		List<PbMessage> msgs = batch.getMessages();
		if(msgs != null && bHead.getLength() > PbMessageTlvFixedLength.BATCH.length()){
			for (PbMessage pbMessage : msgs) {
				
				PbMessageHeader mHead = pbMessage.getHeader();
				long vendor = mHead.getVendorId();
				long type = mHead.getMessageType();
				
				if(valueWriter.containsKey(vendor)){
					if(valueWriter.get(vendor).containsKey(type)){
						mHeadWriter.write(mHead, bOut);
						valueWriter.get(vendor).get(type).write(pbMessage.getValue(), bOut);
					}else{
						throw new SerializationException("Message type is not supported.",false , Long.toString(vendor), Long.toString(type));
					}
				} else {
					throw new SerializationException("Message vendor is not supported.",false, Long.toString(vendor), Long.toString(type));
				}
			}
		}
		
		try {
			bOut.flush();
		} catch (IOException e) {
			throw new SerializationException("Stream could not be flushed.", e, true);
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
