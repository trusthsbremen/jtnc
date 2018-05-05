package de.hsbremen.tc.tnc.im.example.adapter;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

public class TestImcConnection implements IMCConnection, AttributeSupport{

    private final IMV imv;
    private IMVConnection imvc;
    
    public TestImcConnection(IMV imv) {
        this.imv = imv;
    }

    public void setImvConnection(IMVConnection con){
        this.imvc = con;
    }
    
    @Override
    public void sendMessage(long messageType, byte[] message)
            throws TNCException {
        System.err.println("Send Message received from IMC.");
        try {
            this.imv.notifyConnectionChange(this.imvc, TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE);
            this.imv.receiveMessage(this.imvc, messageType, message);
            this.imv.notifyConnectionChange(this.imvc, TNCConstants.TNC_CONNECTION_STATE_ACCESS_ALLOWED);
        } catch (org.trustedcomputinggroup.tnc.ifimv.TNCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void requestHandshakeRetry(long reason) throws TNCException {
        System.err.println("Handshake request received from IMC.");
    }

    @Override
    public Object getAttribute(long attributeID)
            throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
        System.err.println("GetAttribute called with ID: " + attributeID );
        return null;
    }

    @Override
    public void setAttribute(long attributeID, Object attributeValue)
            throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
        System.err.println("SetAttribute called with ID: " + attributeID );
        
    }
    
}
