package de.hsbremen.tc.tnc.tnccs.im.handler;

import java.util.ArrayList;
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

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.im.enums.DefaultImHandlerStateEnum;
import de.hsbremen.tc.tnc.tnccs.im.enums.DefaultImHandlerStateFactory;
import de.hsbremen.tc.tnc.tnccs.im.enums.ImHandlerState;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public class DefaultImvHandler implements ImvHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImvHandler.class);
	
	private Map<Long,ImvAdapter> imAdapters;
	private Map<Long,ImvConnectionAdapter> connections; 
	
	private ImHandlerState state;
	
	private final ImMessageRouter router;
	private ImAdapterManager<ImvAdapter> manager;
	
	private final ImvConnectionAdapterFactory connectionFactory;
	private final ImvConnectionContext connectionContext;

	public DefaultImvHandler(ImAdapterManager<ImvAdapter> manager, 
			ImvConnectionAdapterFactory connectionFactory,
			ImvConnectionContext connectionContext,
			ImMessageRouter router) {
		this.connectionFactory = connectionFactory;
		this.manager = manager;
		this.connectionContext = connectionContext;
		
		Map<Long,ImvAdapter> adapterList = this.manager.getAdapter();
		Map<Long,ImvConnectionAdapter> connectionList = new HashMap<>(adapterList.size()); 
		
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
		
		for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImvAdapter> entry = iter.next();
			
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
	public List<TnccsMessage> requestMessages(){
		this.checkState();

		for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImvAdapter> entry = iter.next();
			
			try {

				if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START)){
					
					try{
						entry.getValue().beginHandshake(this.connections.get(entry.getKey()));
					} catch(UnsupportedOperationException e){
						LOGGER.debug("TNCS first support was not identifiable and the feature is not used.",e);
					}
					
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
		
		if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START)){
			this.state = DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_RUNNING;
		}
		
		return this.connectionContext.clearMessage();
	}

	@Override
	public List<TnccsMessage> forwardMessage(TnccsMessage message) {
		this.checkState();
		
		if(message == null || message.getValue() == null){
			LOGGER.debug("Because Message or message value is null, it is ignored.");
			return new ArrayList<TnccsMessage>();
		}
		
		TnccsMessageValue value = message.getValue();
		
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
			
			if(this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_START)){
				this.state = DefaultImHandlerStateEnum.HSB_SESSION_STATE_HANDSHAKE_RUNNING;
			}
			
		}else{
			LOGGER.debug("Because Message is not of type " + PbMessageValueIm.class.getCanonicalName() + ", it is ignored.");
		}
		
		return this.connectionContext.clearMessage();
	}

	private void refreshAdapterEntries(){
		Map<Long,ImvAdapter> list =  this.manager.getAdapter();

		Set<Long> oldKeys = new HashSet<>(this.imAdapters.keySet());
		Set<Long> newKeys = new HashSet<>(list.keySet());
		newKeys.removeAll(this.imAdapters.keySet());
		oldKeys.removeAll(list.keySet());
		
		for (Long long1 : oldKeys) {
			imAdapters.remove(long1);
			connections.remove(long1);
		}
		
		for (Long long1 : newKeys) {
			ImvAdapter imAdapter = list.get(long1);
			imAdapters.put(imAdapter.getPrimaryId(), imAdapter);
			connections.put(imAdapter.getPrimaryId(), this.connectionFactory.createConnection(imAdapter.getPrimaryId()));
		}
	}
	
	private void checkState(){
		if(this.state == null || this.state.equals(DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN)){
			throw new IllegalStateException("The handler's state cannot be:" + ((this.state != null)? this.state : DefaultImHandlerStateEnum.HSB_SESSION_STATE_UNKNOWN).toString());
		}
	}

	@Override
	public List<ImvRecommendationPair> solicitRecommendation() {
		this.checkState();
		
		for (Iterator<Entry<Long, ImvAdapter>> iter = this.imAdapters.entrySet().iterator(); iter.hasNext(); ) {
			Entry<Long, ImvAdapter> entry = iter.next();
			
			try {

				entry.getValue().solicitRecommendation(this.connections.get(entry.getKey()));

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
		
		return this.connectionContext.clearRecommendations();
	}
}
