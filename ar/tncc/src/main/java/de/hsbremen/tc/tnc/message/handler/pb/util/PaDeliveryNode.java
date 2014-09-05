package de.hsbremen.tc.tnc.message.handler.pb.util;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.TNCException;

import de.hsbremen.tc.tnc.tncc.session.TncSession;

public interface PaDeliveryNode {

    
    public abstract void register(ImcContainer imc);
    
    public abstract void unRegister(ImcContainer imc);
    
    //public abstract ImcMessageDistributorIf getRegistered();
    
    public abstract long getId();

    public abstract void dispatchMessage(PbMessageValueIm message, TncSession context) throws TNCException;
    
    public abstract long getElementCount();
}
