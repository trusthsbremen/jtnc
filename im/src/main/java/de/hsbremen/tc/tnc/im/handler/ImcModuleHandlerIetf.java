package de.hsbremen.tc.tnc.im.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.command.FunctionCall;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterBuilder;
import de.hsbremen.tc.tnc.im.command.ImcFunctionCallBuilder;
import de.hsbremen.tc.tnc.im.exception.HandshakeAlreadyStartedException;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.route.ImMessageRouteComponent;
import de.hsbremen.tc.tnc.im.route.ImMessageRouteFactory;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImcModuleHandlerIetf implements ImModuleHandler<IMC>, ImConnectionMessageObserver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImcModuleHandlerIetf.class);
	
	private long connectionState;
	
	private Map<ImModule<IMC>,IMCConnection> imcs;
	private List<TnccsMessageValue> returnMessages;
	private ImMessageRouteComponent<ImModule<IMC>> routingTable;
	
	private ImcFunctionCallBuilder functionCallBuilder;
	private ImcConnectionAdapterBuilder connectionAdapterBuilder;
	
	public ImcModuleHandlerIetf(ImcConnectionAdapterBuilder builder, ImMessageRouteFactory routeFactory, ImcFunctionCallBuilder functionBuilder){
		this.imcs = new HashMap<>();
		this.functionCallBuilder = functionBuilder;
		this.connectionAdapterBuilder = builder;
		this.routingTable = routeFactory.createRoutingTable();
	
		this.connectionState = TNCConstants.TNC_CONNECTION_STATE_CREATE;
	}
	
	/* ImModuleHandler */
	
	@Override
	public List<TnccsMessageValue> startHandshake(){
		this.returnMessages = new ArrayList<>();
		this.connectionState = TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE;
		for (Iterator<Entry<ImModule<IMC>,IMCConnection>> iter = this.imcs.entrySet().iterator(); iter.hasNext();) {
			Entry<ImModule<IMC>,IMCConnection> entry = iter.next();
			try{
				entry.getKey().getIm().notifyConnectionChange(entry.getValue(), TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE);
				FunctionCall begin = this.functionCallBuilder.buildBeginHandshakeCall(entry.getKey().getIm(), entry.getValue());
				begin.call();
				
			}catch(TNCException e){
				// log and remove ImModule form further handling with this handler.
				LOGGER.error("An exception has occured, while trying to begin the handshake with the IMC #" + entry.getKey().getPrimaryId() +".", e);
				this.handleExceptionWhileIterating(iter, entry);
			}
		}
		return this.returnMessages;
	}

	@Override
	public List<TnccsMessageValue> forwardMessages(
			List<TnccsMessageValue> values) {
		this.returnMessages = new ArrayList<>();
		
		
		if(values != null){
			// TNCS has send first switch connection state.
			if(this.connectionState != TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE){
				this.connectionState = TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE;
				for (Iterator<Entry<ImModule<IMC>,IMCConnection>> iter = this.imcs.entrySet().iterator(); iter.hasNext();) {
					Entry<ImModule<IMC>,IMCConnection> entry = iter.next();
					try{
						entry.getKey().getIm().notifyConnectionChange(entry.getValue(), TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE);
					}catch (TNCException e){
						// log and remove ImModule form further handling with this handler.
						LOGGER.error("An exception has occured, while trying to begin the handshake with the IMC #" + entry.getKey().getPrimaryId() +".", e);
						this.handleExceptionWhileIterating(iter, entry);
					}
				}
			}
			
			for (TnccsMessageValue value : values) {
				this.handleMessageValue(value);
			}
		}
		
		for (Iterator<Entry<ImModule<IMC>,IMCConnection>> iter = this.imcs.entrySet().iterator(); iter.hasNext();) {
			Entry<ImModule<IMC>,IMCConnection> entry = iter.next();
			try{
				
				FunctionCall end = this.functionCallBuilder.buildBatchEndingCall(entry.getKey().getIm(), entry.getValue());
				end.call();
				
			}catch(TNCException e){
				// log and remove ImModule form further handling with this handler.
				LOGGER.error("An exception has occured, while trying to notify the IMC #" + entry.getKey().getPrimaryId() +" about the batch ending.", e);
				this.handleExceptionWhileIterating(iter, entry);
			}
		}
		
		return this.returnMessages;
	}

	@Override
	public void endHandshake(long result) {
		this.returnMessages = new ArrayList<>();
		for (Iterator<Entry<ImModule<IMC>,IMCConnection>> iter = this.imcs.entrySet().iterator(); iter.hasNext();) {
			Entry<ImModule<IMC>,IMCConnection> entry = iter.next();
			try{
				
				entry.getKey().getIm().notifyConnectionChange(entry.getValue(), result);
				
			}catch(TNCException e){
				// log and remove ImModule form further handling with this handler.
				LOGGER.error("An exception has occured, while trying to notify the IMC #" + entry.getKey().getPrimaryId() +" about handshake end.", e);
				this.handleExceptionWhileIterating(iter, entry);
			}
		}
		this.connectionState = result;
	}
	
	@Override
	public void subscribe(ImModule<IMC> module) throws HandshakeAlreadyStartedException {
		if(this.connectionState == TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE){
			throw new HandshakeAlreadyStartedException("Handshake has already started, no other Modules can be added.");
		}
		
		if(module != null && !this.imcs.containsKey(module)){
			// add to map
			this.imcs.put(module,connectionAdapterBuilder.buildAdapter(module.getIm(), this));
		
			// add to routing
			List<SupportedMessageType> sMessages = module.getSupportedMessageTypes();
			for (SupportedMessageType smt : sMessages) {
				this.routingTable.subscribe(module, smt.getVendorId(), smt.getType());
			}
			
		} // else ignore
		
	}

	@Override
	public void unSubscribe(ImModule<IMC> module) {
		if(module != null && this.imcs.containsKey(module)){
		
			// remove from routing
			List<SupportedMessageType> sMessages = module.getSupportedMessageTypes();
			for (SupportedMessageType smt : sMessages) {
				this.routingTable.unSubscribe(module, smt.getVendorId(), smt.getType());
			}
			
			// remove from map
			this.imcs.remove(module);
			
		}// else ignore
		
	}
	
	protected void handleMessageValue(TnccsMessageValue value){
		if(value != null && value instanceof PbMessageValueIm){
			
			PbMessageValueIm val = (PbMessageValueIm) value;
			List<ImModule<IMC>> recipient = routingTable.findRecipients(val.getSubVendorId(), val.getSubType());
			
			for (Iterator<ImModule<IMC>> iter = recipient.iterator(); iter.hasNext();) {
				ImModule<IMC> imModule = iter.next();
				try{
					
					FunctionCall receive = this.functionCallBuilder.buildReceiveMessageCall(imModule.getIm(), this.imcs.get(imModule), val);
					receive.call();
				
				}catch(TNCException e){
					// log and remove ImModule form further handling with this handler.
					LOGGER.error("An exception has occured, while trying to send a message to the IMC #" + imModule.getPrimaryId() +".", e);
					List<SupportedMessageType> sMessages = imModule.getSupportedMessageTypes();
					for (SupportedMessageType smt : sMessages) {
						this.routingTable.unSubscribe(imModule, smt.getVendorId(), smt.getType());
					}
					this.imcs.remove(imModule);
					iter.remove();
				}
			}
	
		}
	}
	
	
	private void handleExceptionWhileIterating(Iterator<Entry<ImModule<IMC>, IMCConnection>> iter,Entry<ImModule<IMC>, IMCConnection> entry){
		// remove from routing
		List<SupportedMessageType> sMessages = entry.getKey().getSupportedMessageTypes();
		for (SupportedMessageType smt : sMessages) {
			this.routingTable.unSubscribe(entry.getKey(), smt.getVendorId(), smt.getType());
		}
		iter.remove();
		
	}
	
	/* ConnectionMessageObserver */
	
	@Override
	public void addReturnValue(TnccsMessageValue value){
		if(value != null){
			this.returnMessages.add(value);
		}
	}
}
