package de.hsbremen.tc.tnc.im.example.adapter;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

public class TestImvConnection implements IMVConnection{

    private IMCConnection imcc;
    private final IMC imc;
    
    public TestImvConnection(IMC imc) {
        this.imc = imc;
    }

    public void setImcConnection(IMCConnection con){
        this.imcc = con;
    }
    
    @Override
    public void sendMessage(long messageType, byte[] message)
            throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
        System.err.println("Send Message received from IMV.");
        try {
            this.imc.receiveMessage(this.imcc, messageType, message);
            this.imc.notifyConnectionChange(this.imcc, TNCConstants.TNC_CONNECTION_STATE_ACCESS_ALLOWED);
        } catch (TNCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void requestHandshakeRetry(long reason)
            throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
        System.err.println("Handshake request received from IMV.");
        
    }

    @Override
    public void provideRecommendation(long recommendation, long evaluation)
            throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
        System.err.println("Recommendation provided at IMVConnection:\n" + ImvActionRecommendationEnum.fromId(recommendation) +", "+  ImvEvaluationResultEnum.fromId(evaluation) );
    }

    @Override
    public Object getAttribute(long attributeID)
            throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
        System.err.println("GetAttribute called with ID: " + attributeID );
        return null;
    }

    @Override
    public void setAttribute(long attributeID, Object attributeValue)
            throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
        System.err.println("SetAttribute called with ID: " + attributeID );
        
    }
    
}