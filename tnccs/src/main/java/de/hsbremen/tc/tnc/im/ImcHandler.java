package de.hsbremen.tc.tnc.im;

import java.util.List;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImcHandler {
	
	private ImxManager manager;
	private SomeSession session;
	
	public void triggerMessage(ImMessageTriggerEnum trigger) throws TncException{
		
		Set<IMC> keySet = this.calculateSubscriptionSet(connection);
		
		for (IMC imc : keySet) {
			try {
				if(trigger.equals(ImMessageTriggerEnum.BEGIN_HANDSHAKE)){
					this.loadedModules.get(imc).getIm().beginHandshake(connection);
				}else if (trigger.equals(ImMessageTriggerEnum.BATCH_ENDING)){
					this.loadedModules.get(imc).getIm().batchEnding(connection);
				}
			} catch (TncException e) {
				if(e.getResultCode() == TncExceptionCodeEnum.TNC_RESULT_FATAL){
					LOGGER.error("IMC/V reported a fatal error. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					this.handleFaultyIm(imc,removed);
					
				}else if( e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_NOT_INITIALIZED)){
					LOGGER.error("IMC/V was not initialized. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					
				}else{
					throw e;
				}
			}
		}

	}

	public void handleMessage(IMCConnection connection, TnccsMessageValue value) throws TncException{
		if(!this.hasSubscription(connection)){
			throw new IllegalAccessError("The connection " + connection.toString() +" has no valid subscription.");
		}
		
		Set<IMC> keySet = this.calculateSubscriptionSet(connection);
		// TODO make this better, such as refactor it to the router class. The router needs to know all IMC IDs.
		if(value instanceof PbMessageValueIm){
			if(((PbMessageValueIm)value).getImFlags().contains(PbMessageImFlagsEnum.EXCL)){
				long collectorId = ((PbMessageValueIm)value).getCollectorId();
				if( collectorId != TNCConstants.TNC_IMCID_ANY){
					for (IMC imc : keySet) {
						if(this.loadedModules.get(imc).getIds().contains(collectorId)){
							this.loadedModules.get(imc).getIm().handleMessage(connection, value);
							return;
						}
					}
				}
			}
		}
		List<IMC> recipientList = this.router.findRecipients(keySet, value);
		for (IMC imc : recipientList) {
			try {
				this.loadedModules.get(imc).getIm().handleMessage(connection, value);
			} catch (TncException e) {
				// unload faulty Im and throw exception
				if(e.getResultCode() == TncExceptionCodeEnum.TNC_RESULT_FATAL){
					LOGGER.error("IMC/V reported a fatal error. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					this.handleFaultyIm(imc,removed);
					
				}else if( e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_NOT_INITIALIZED)){
					LOGGER.error("IMC/V was not initialized. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					
				}else{
					throw e;
				}
			}
		}
	}
	
	// IMCConnection is the wrong ID handle, because I have an IMCConnection per IMC. In this Setup I should have only one IMCConnection for all
	 
	public void setConnectionState(IMCConnection connection, TncConnectionState state) throws TncException{
		if(!this.hasSubscription(connection)){
			throw new IllegalAccessError("The connection " + connection.toString() +" has no valid subscription.");
		}
		
		Set<IMC> keySet = this.calculateSubscriptionSet(connection);
		
		for (IMC imc : keySet) {
			try {
				this.loadedModules.get(imc).getIm().notifyConnectionChange(connection, state);
			} catch (TncException e) {
				// unload faulty Im and throw exception
				if(e.getResultCode() == TncExceptionCodeEnum.TNC_RESULT_FATAL){
					LOGGER.error("IMC/V reported a fatal error. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					this.handleFaultyIm(imc,removed);
					
				}else if( e.getResultCode().equals(TncExceptionCodeEnum.TNC_RESULT_NOT_INITIALIZED)){
					LOGGER.error("IMC/V was not initialized. The IMC/V will be removed.",e);
					this.subscriptions.get(connection).remove(imc);
					Imc removed = this.loadedModules.remove(imc);
					this.router.removeFromMap(imc,removed);
					
				}else{
					throw e;
				}
			}
		}
	}
	
}
