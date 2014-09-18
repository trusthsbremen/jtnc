package org.ietf.nea.pb.handler.util;

import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCException;

import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcSupportedMessageType;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;

public class PaDeliveryNodeVendor implements PaDeliveryNode{

    private long vendorId;
    
    Map<Long, PaDeliveryNode> distributors;
    
    public PaDeliveryNodeVendor(long vendorId) {
       this.vendorId = vendorId;
    }

    @Override
    public void register(ImcContainer imc) {

        List<ImcSupportedMessageType> supportedMessageTypes = imc.getSupportedMessageTypes();
        // TODO if a vendor id and any vendor would be set, the imc will get the message two time
        // If known that this may happen this function or the ImcContainer should be changed to filter this.
        for (ImcSupportedMessageType imcMessageType : supportedMessageTypes) {

            long type = imcMessageType.getType();

            if (!distributors.containsKey(type)) {
                distributors.put(type, new PaDeliveryNodeType(type));
            }

            distributors.get(type).register(imc);

        }

    }

    @Override
    public void unRegister(ImcContainer imc) {
       
        List<ImcSupportedMessageType> supportedMessageTypes = imc.getSupportedMessageTypes();
        // TODO if a vendor id and any vendor would be set, the imc will get the message two time
        // If known that this may happen this function or the ImcContainer should be changed to filter this.
        for (ImcSupportedMessageType imcMessageType : supportedMessageTypes) {
            
            long type = imcMessageType.getType();
          
            if(distributors.containsKey(type)){
                distributors.get(type).unRegister(imc); 
                if(distributors.get(type).getElementCount() == 0){
                    distributors.remove(type);
                }
            }
        }
        
    }

    @Override
    public long getId() {
        return this.vendorId;
    }

    @Override
    public void dispatchMessage(TnccsMessageIm message, TnccsSession context) throws TNCException {
        if(distributors.containsKey(message.getSubType())){
            distributors.get(message.getSubType()).dispatchMessage(message,context);
        } 
        
    }

    @Override
    public long getElementCount() {
        return this.distributors.size();
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    @Override
//    public void update(Observable arg0, Object arg1) {
//       TnccsMessageIm message = (TnccsMessageIm)arg1; 
//       
//        
//    }
//    
//    @Override
//    public void addObserver(Observer o){
//        super.addObserver(o);
//        registerImc((ImcContainer)o);
//    }
//    
//    @Override
//    public void deleteObserver(Observer o){
//        super.deleteObserver(o);
//        deregisterImc((ImcContainer)o);
//    }
//    
//    
//    private void registerImc(ImcContainer imc){
//        long[] supportedMessageTypes = imc.getSupportedMessageTypes();
//        for (long l : supportedMessageTypes) {
//            long messageType = (l & IETFConstants.RESERVED_16);
//            if(distributors.containsKey(messageType) || (messageType == IETFConstants.RESERVED_16)){
//                distributors.get(messageType).addObserver(imc);
//            }
//        }
//    }
//    
//    private void deregisterImc(ImcContainer imc){
//        long[] supportedMessageTypes = imc.getSupportedMessageTypes();
//        for (long l : supportedMessageTypes) {
//            long messageType = (l & IETFConstants.RESERVED_16);
//            if(distributors.containsKey(messageType) || (messageType == IETFConstants.RESERVED_16)){
//                distributors.get(messageType).deleteObserver(imc);
//            }
//        }
//    }
//
//    public void distribute(TnccsMessageIm message) {
//        if(distributors.containsKey(message.getSubVendorId())){
//            distributors.get(message.getSubVendorId()).distribute(message);
//        }
//        
//    }
//    
}
