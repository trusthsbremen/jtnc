package de.hsbremen.tc.tnc.message.handler.pb.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcSupportedMessageType;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.ReceiveMessage;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;

public class PaDeliveryAgent implements PaDeliveryNode{

    
    private Map<Long, PaDeliveryNode> distributors;
    
    
    public void ImcMessageTypeDistributor(TnccsSession context){
        this.distributors = new HashMap<Long, PaDeliveryNode>();
    }

    @Override
    public void register(ImcContainer imc) {

        List<ImcSupportedMessageType> supportedMessageTypes = imc.getSupportedMessageTypes();
        // TODO if a vendor id and any vendor would be set, the imc will get the message two time
        // If known that this may happen this function or the ImcContainer should be changed to filter this.
        for (ImcSupportedMessageType imcMessageType : supportedMessageTypes) {

            long vendorId = imcMessageType.getVendorId();

            if (!distributors.containsKey(vendorId)) {
                distributors.put(vendorId, new PaDeliveryNodeVendor(vendorId));
            }

            distributors.get(vendorId).register(imc);

        }

    }

    @Override
    public void unRegister(ImcContainer imc) {
       
        List<ImcSupportedMessageType> supportedMessageTypes = imc.getSupportedMessageTypes();
        // TODO if a vendor id and any vendor would be set, the imc will get the message two time
        // If known that this may happen this function or the ImcContainer should be changed to filter this.
        for (ImcSupportedMessageType imcMessageType : supportedMessageTypes) {
            
            long vendorId = imcMessageType.getVendorId();
          
            if(distributors.containsKey(vendorId)){
                distributors.get(vendorId).unRegister(imc); 
                if(distributors.get(vendorId).getElementCount() == 0){
                    distributors.remove(vendorId);
                }
            }
        }
        
    }

    @Override
    public void dispatchMessage(TnccsMessageIm message, TnccsSession context) throws TNCException {
        
        // if exclusive send directly
        if ((message.getImFlags() & TnccsMessageIm.EXCL) != 0){
            this.dispatchMessageDirectly(message, context);
        }else{
            // if not distribute to subscribers
            if(distributors.containsKey(message.getSubVendorId())){
                distributors.get(message.getSubVendorId()).dispatchMessage(message,context);
            } 
        }
        // if nothing matches ignore message
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException("The method getId is not support by compsite root class: "+ PaDeliveryNodeType.class.getName());
    }

    @Override
    public long getElementCount() {
        throw new UnsupportedOperationException("The method getElementCount is not support by compsite root class: "+ PaDeliveryNodeType.class.getName()); 
    }

    private void dispatchMessageDirectly(TnccsMessageIm message, TnccsSession context) throws TNCException {
        List<ImcContainer> imcList = context.getKnownImc();

        for (ImcContainer imcContainer : imcList) {

            if (imcContainer.getImcIds().contains(message.getCollectorId())) {

                    IMCConnection connection = context.getSessionImcConnections().get(imcContainer.getPrimaryId());
                    //Use executor to execute a command and stop it after a fixed time length.
                    AbstractTimedImcFunctionCall receiveMessage = new ReceiveMessage(imcContainer, connection, message);
                    receiveMessage.callFunction();
            }//TODO else throw error or ignore?
        }
    }
}
