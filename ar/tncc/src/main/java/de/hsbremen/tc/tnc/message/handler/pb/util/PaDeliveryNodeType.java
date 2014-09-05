package de.hsbremen.tc.tnc.message.handler.pb.util;

import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.ReceiveMessage;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;

public class PaDeliveryNodeType implements PaDeliveryNode {

    private long messagetype;

    // Every IMC should subscribe itself once to a message.
    private Set<ImcContainer> subscribers;

    public PaDeliveryNodeType(long messagetype) {
        this.messagetype = messagetype;
        this.subscribers = new HashSet<>();
    }

    @Override
    public void register(ImcContainer imc) {
        this.subscribers.add(imc);

    }

    @Override
    public void unRegister(ImcContainer imc) {

        if (subscribers.contains(imc)) {
            subscribers.remove(imc);
        }

    }

    @Override
    public long getId() {
        return this.messagetype;
    }

    @Override
    public void dispatchMessage(TnccsMessageIm message, TnccsSession context) throws TNCException {
  
        for (ImcContainer imcContainer : subscribers) {

            IMCConnection connection = context.getSessionImcConnections().get(imcContainer.getPrimaryId());
            AbstractTimedImcFunctionCall receiveMessage = new ReceiveMessage(imcContainer, connection, message);
            receiveMessage.callFunction();
        }
    }
            

    @Override
    public long getElementCount() {
        return subscribers.size();
    }

}
