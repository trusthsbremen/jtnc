package de.hsbremen.tc.tnc.newp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.adapter.im.TerminatedException;
import de.hsbremen.tc.tnc.connection.ImConnectionState;
import de.hsbremen.tc.tnc.connection.ImTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class DefaultImcHandler implements ImHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcHandler.class);
	private Map<Long,ImAdapter<IMCConnection>> imAdapters;
	private Map<Long,ImcConnectionAdapter> connections; 
	private ImMessageRouter router;
	private ImAdapterManager manager;
	private ImcConnectionAdapterFactory connectionFactory;

	public DefaultImcHandler(ImAdapterManager manager, 
			ImcConnectionAdapterFactory connectionFactory,
			ImMessageRouter router) {
		this.connectionFactory = connectionFactory;
		this.manager = manager;
		
		List<ImAdapter<IMCConnection>> list = (List<ImAdapter<IMCConnection>>) this.manager.getAdapter();
		Map<Long,ImAdapter<IMCConnection>> imAdapters = new HashMap<>(list.size());
		Map<Long,ImcConnectionAdapter> connections = new HashMap<>(list.size()); 
		
		for (ImAdapter<IMCConnection> imAdapter : list) {
			imAdapters.put(imAdapter.getPrimaryId(), imAdapter);
			connections.put(imAdapter.getPrimaryId(), this.connectionFactory.createConnection(imAdapter.getPrimaryId()));
		}
		
		this.imAdapters = imAdapters;
		this.connections = connections;
		this.router = router;
	}

	@Override
	public void setConnectionState(ImConnectionState imConnectionState){
		
		if(imConnectionState.state() == ImTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE.state()){
			this.refreshAdapterEntries();
		}
		
		for (Iterator<Entry<Long, ImAdapter<IMCConnection>>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImAdapter<IMCConnection>> entry = iter.next();
			try {
				entry.getValue().notifyConnectionChange(this.connections.get(entry.getKey()), imConnectionState);
			} catch (TerminatedException e) {
				this.connections.remove(entry.getKey());
				iter.remove();
			} catch (TncException e){
				if(e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_FATAL)){
					this.connections.remove(entry.getKey());
					this.manager.removeAdapter(entry.getKey());
					iter.remove();
					
				}
				LOGGER.error(e.getMessage(),e);
			}
		}
		
		if(imConnectionState.state() == ImTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE.state()){
			this.imAdapters.clear();
			this.connections.clear();
		}
		
	}

	@Override
	public void triggerMessage(TnccsSessionContext context,
			ImMessageTriggerEnum trigger){
		for (Iterator<Entry<Long, ImAdapter<IMCConnection>>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImAdapter<IMCConnection>> entry = iter.next();
			try {
				this.connections.get(entry.getKey()).allowMessageReceipt();
				if(trigger.equals(ImMessageTriggerEnum.BEGIN_HANDSHAKE)){
					entry.getValue().beginHandshake(this.connections.get(entry.getKey()));
				}else if(trigger.equals(ImMessageTriggerEnum.BATCH_ENDING)){
					entry.getValue().batchEnding(this.connections.get(entry.getKey()));
				}
			} catch (TerminatedException e) {
				this.connections.remove(entry.getKey());
				iter.remove();
			} catch (TncException e){
				if(e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_FATAL)){
					this.connections.remove(entry.getKey());
					this.manager.removeAdapter(entry.getKey());
					iter.remove();
				}
				LOGGER.error(e.getMessage(),e);
			}
		}
	}

	@Override
	public void handleMessage(TnccsSessionContext context,
			TnccsMessageValue value) {
		if(value instanceof PbMessageValueIm){
			PbMessageValueIm valueCast = (PbMessageValueIm)value;
			Set<Long> recipients = new HashSet<>();
			if(valueCast.getImFlags().contains(PbMessageImFlagsEnum.EXCL)){
				Long recipient = this.router.findExclRecipientId(valueCast.getCollectorId(),valueCast.getSubVendorId(), valueCast.getSubType());
				if(recipient != null){
					recipients.add(recipient);
				}
			}else{
				recipients = this.router.findRecipientIds(valueCast.getSubVendorId(), valueCast.getSubType());
			}
			for (Long recipientId : recipients) {
				if(this.imAdapters.containsKey(recipientId)){
					try {
						this.imAdapters.get(recipientId).handleMessage(this.connections.get(recipientId), value);
					} catch (TerminatedException e) {
						this.imAdapters.remove(recipientId);
						this.connections.remove(recipientId);
					} catch (TncException e){
						if(e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_FATAL)){
							this.connections.remove(recipientId);
							this.manager.removeAdapter(recipientId);
							this.imAdapters.remove(recipientId);
							
						}
						LOGGER.error(e.getMessage(),e);
					}
				}
			}
		}else{
			LOGGER.debug("Because Message is not of type " + PbMessageValueIm.class.getCanonicalName() + ", it is ignored.");
		}
	}

	private void refreshAdapterEntries(){
		Map<Long, ? extends ImAdapter<?>> list =  this.manager.getAdapter();

		Set<Long> oldKeys = new HashSet<>(this.imAdapters.keySet());
		Set<Long> newKeys = new HashSet<>(list.keySet());
		newKeys.removeAll(this.imAdapters.keySet());
		oldKeys.removeAll(list.keySet());
		
		for (Long long1 : oldKeys) {
			imAdapters.remove(long1);
			connections.remove(long1);
		}
		
		for (Long long1 : newKeys) {
			ImAdapter<IMCConnection> imAdapter = (ImAdapter<IMCConnection>)list.get(long1);
			imAdapters.put(imAdapter.getPrimaryId(), imAdapter);
			connections.put(imAdapter.getPrimaryId(), this.connectionFactory.createConnection(imAdapter.getPrimaryId()));
		}
	}
}
