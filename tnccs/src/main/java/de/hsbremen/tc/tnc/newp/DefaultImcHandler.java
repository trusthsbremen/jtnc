package de.hsbremen.tc.tnc.newp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.adapter.im.TerminatedException;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.newp.enums.DefaultImHandlerStateEnum;
import de.hsbremen.tc.tnc.newp.enums.DefaultImHandlerStateFactory;
import de.hsbremen.tc.tnc.newp.enums.ImHandlerState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class DefaultImcHandler implements ImHandler2{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcHandler.class);
	private Map<Long,ImcAdapter> imAdapters;
	private Map<Long,ImcConnectionAdapter> connections; 
	
	private ImHandlerState state;
	
	private ImMessageRouter router;
	private ImAdapterManager<ImcAdapter> manager;
	
	private ImcConnectionAdapterFactory connectionFactory;
	

	public DefaultImcHandler(ImAdapterManager<ImcAdapter> manager, 
			ImcConnectionAdapterFactory connectionFactory,
			ImMessageRouter router) {
		this.connectionFactory = connectionFactory;
		this.manager = manager;
		
		Map<Long,ImcAdapter> adapterList = this.manager.getAdapter();
		Map<Long,ImcConnectionAdapter> connectionList = new HashMap<>(adapterList.size()); 
		
		for (Long key : adapterList.keySet()) {
			connectionList.put(adapterList.get(key).getPrimaryId(), this.connectionFactory.createConnection(adapterList.get(key).getPrimaryId()));
		}
		
		this.imAdapters = adapterList;
		this.connections = connectionList;
		this.router = router;

		this.state = DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN;
		
	}

	@Override
	public void setConnectionState(TncConnectionState imConnectionState){
		
		this.state = DefaultImHandlerStateFactory.getInstance().fromConnectionState(imConnectionState);
		
		if(imConnectionState.state() == DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE.state()){
			this.refreshAdapterEntries();
		}
		
		for (Iterator<Entry<Long, ImcAdapter>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImcAdapter> entry = iter.next();
			
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
		
		if(imConnectionState.state() == DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE.state()){
			this.imAdapters.clear();
			this.connections.clear();
		}
		
	}

	@Override
	public void requestMessages(TnccsSessionContext context){
		this.checkState();

		for (Iterator<Entry<Long, ImcAdapter>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImcAdapter> entry = iter.next();
			
			try {

				if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START)){
					
					entry.getValue().beginHandshake(this.connections.get(entry.getKey()));
					this.state = DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_RUNNING;
					
				}else if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_RUNNING)){
					
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
	public void forwardMessage(TnccsSessionContext context, TnccsMessageValue value) {
		this.checkState();
		
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
						if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START)){
							this.state = DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_RUNNING;
						}
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
		Map<Long,ImcAdapter> list =  this.manager.getAdapter();

		Set<Long> oldKeys = new HashSet<>(this.imAdapters.keySet());
		Set<Long> newKeys = new HashSet<>(list.keySet());
		newKeys.removeAll(this.imAdapters.keySet());
		oldKeys.removeAll(list.keySet());
		
		for (Long long1 : oldKeys) {
			imAdapters.remove(long1);
			connections.remove(long1);
		}
		
		for (Long long1 : newKeys) {
			ImcAdapter imAdapter = list.get(long1);
			imAdapters.put(imAdapter.getPrimaryId(), imAdapter);
			connections.put(imAdapter.getPrimaryId(), this.connectionFactory.createConnection(imAdapter.getPrimaryId()));
		}
	}
	
	private void checkState(){
		if(this.state == null || this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN)){
			throw new IllegalStateException("The handlers state cannot be:" + ((this.state != null)? this.state : DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN).toString());
		}
	}
}
