package de.hsbremen.tc.tnc.session.state;

import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCConstants;
import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.message.tnccs.TnccsAbstractMessage;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsBatch;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcMessageBuffer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;
import de.hsbremen.jtnc.ar.tncc.session.timed.BatchEnding;

public class StateEnd implements TnccsState {

    private TnccsBatch closeBatch;
    
    public StateEnd (TnccsBatch closeBatch){
        this.closeBatch = closeBatch;
    }
   
    public StateEnd() {
        this.closeBatch = null;
    }

    @Override
    public TnccsState handle(TnccsSession context) {
        // TODO Auto-generated method stub
        TnccsSession session = context;
        if(this.closeBatch != null ){
            // Work Thru messages (should only be errors)
            this.deliverMessages(this.closeBatch.getMessages(), session);
            // Messages are send after the batch ending is informed
            this.informBatchEnding(session);
            // remove batch after delivery
            this.closeBatch = null;
        }
        // Finally inform of connection end.
        this.informEnd(session);
        // and set session to shutdown for execution
        session.setShutdown(true);
        // Do not send any more batches because close was send.
        //send close batch
        return this;
    }
    
    private void deliverMessages(List<TnccsAbstractMessage> messages, TnccsSession session) {
        for (TnccsAbstractMessage message : messages) {
            try {
                session.distributeMessageToImc(message);
            } catch (TNCException e) {
                // TODO Auto-generated catch block
                // Log and add error message to batch
                e.printStackTrace();
            }
        }
    }

    private void informBatchEnding(TnccsSession session) {

        Map<Long, IMCConnection> sessionConnections = session.getSessionImcConnections();
        List<ImcContainer> imcList = session.getKnownImc();

        for (ImcContainer imcContainer : imcList) {
            if (sessionConnections.containsKey(imcContainer.getPrimaryId())) {

                IMCConnection connection = sessionConnections.get(imcContainer.getPrimaryId());
                // Use executor to execute a command and stop it after a fixed time length.
                AbstractTimedImcFunctionCall batchEnding = new BatchEnding(imcContainer, connection);
                batchEnding.callFunction();
                // Purge all new messages because nothing should be send here
                ((ImcMessageBuffer) connection).clearBuffer();
            }
        }
    }
    
    private void informEnd(TnccsSession session){
        
        Map<Long, IMCConnection> sessionConnections = session.getSessionImcConnections();
        List<ImcContainer> imcList = session.getKnownImc();
        
        for (ImcContainer imcContainer : imcList) {
            if(sessionConnections.containsKey(imcContainer.getPrimaryId())){
                
                IMCConnection connection = sessionConnections.get(imcContainer.getPrimaryId());
                //Use executor to execute a command and stop it after a fixed time length.
                try {
                    imcContainer.getImc().notifyConnectionChange(connection,
                            TNCConstants.TNC_CONNECTION_STATE_DELETE);
                } catch (TNCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            }
        }
    }
}
