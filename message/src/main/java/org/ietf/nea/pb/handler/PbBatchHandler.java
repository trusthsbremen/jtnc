package org.ietf.nea.pb.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.handler.filter.MessageFilter;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.exception.HandlingException;
import de.hsbremen.tc.tnc.tnccs.handler.TnccsHandler;
import de.hsbremen.tc.tnc.util.Combined;

public class PbBatchHandler implements TnccsHandler<PbBatch>, Combined<TnccsHandler<PbMessageValue>> {

	private Map<Long,Map<Long,TnccsHandler<PbMessageValue>>> pbMessageHandlers;
	private MessageFilter filter;
	
	public PbBatchHandler(){
		this(null);
	}
	
	public PbBatchHandler(MessageFilter filter){
		this.pbMessageHandlers = new HashMap<>();
		this.filter = filter;
	}
	
	@Override
	public void handle(PbBatch data) throws HandlingException {
		if(data == null){
			throw new NullPointerException("Batch cannot be NULL.");
		}
		
		this.checkCanHandleNoSkips(data);
		//this.filterMessagesByBatchType(messages);
		this.dispatchMessagesToHandler(data);
	}

	private void checkCanHandleNoSkips(PbBatch batch) throws HandlingException{
		List<PbMessage> messages = batch.getMessages();
		for (PbMessage pbMessage : messages) {
			if(pbMessage.getHeader().getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
				if(!pbMessageHandlers.containsKey(pbMessage.getHeader().getVendorId())){
					throw new HandlingException("One message has NOSKIP set, but cannot be handled by one of the handlers.", Long.toString(pbMessage.getHeader().getVendorId()));
				}
				if(!pbMessageHandlers.get(pbMessage.getHeader().getVendorId()).containsKey(pbMessage.getHeader().getMessageType())){
					throw new HandlingException("One message has NOSKIP set, but cannot be handled by one of the handlers.", Long.toString(pbMessage.getHeader().getVendorId()), Long.toString(pbMessage.getHeader().getMessageType()));
				}
			}
		}
	}

	private void dispatchMessagesToHandler(PbBatch batch) throws HandlingException{
		List<PbMessage> messages = batch.getMessages();
		for (PbMessage pbMessage : messages) {
			if(pbMessageHandlers.containsKey(pbMessage.getHeader().getVendorId())){
				if(pbMessageHandlers.get(pbMessage.getHeader().getVendorId()).containsKey(pbMessage.getHeader().getMessageType())){
					if(filter == null || filter.noFilter(batch, pbMessage)){
						pbMessageHandlers.get(pbMessage.getHeader().getVendorId()).get(pbMessage.getHeader().getMessageType()).handle(pbMessage.getValue());
					}
					// Else Ignore because message should be ignored, see RFC5793.
				}
				// Else ignore because NOSKIP is safe and unknown messages should be ignored, see RFC5793 and checkCanHandleNoSkips.
			}
			// Else ignore because NOSKIP is safe and unknown messages should be ignored, see RFC5793 and checkCanHandleNoSkips.
		}
		
	}

	@Override
	public void add(Long vendorId, Long messageType,
			TnccsHandler<PbMessageValue> handler) {
		if(pbMessageHandlers.containsKey(vendorId)){
			pbMessageHandlers.get(vendorId).put(messageType, handler);
		}else{
			pbMessageHandlers.put(vendorId, new HashMap<Long, TnccsHandler<PbMessageValue>>());
			pbMessageHandlers.get(vendorId).put(messageType, handler);
		}
	}


	@Override
	public void remove(Long vendorId, Long messageType) {
		if(pbMessageHandlers.containsKey(vendorId)){
			if(pbMessageHandlers.get(vendorId).containsKey(messageType)){
				pbMessageHandlers.get(vendorId).remove(messageType);
			}
			if(pbMessageHandlers.get(vendorId).isEmpty()){
				pbMessageHandlers.remove(vendorId);
			}
		}
		
	}
}
