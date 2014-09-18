package de.hsbremen.tc.tnc.session.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCConstants;
import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.exception.tncc.ImcMessageBufferMandatoryException;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchDirectionalityEnum;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchTypeEnum;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsAbstractMessage;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsBatch;
import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.message.validator.TnccsHeaderValidatorFactory;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcMessageBuffer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;
import de.hsbremen.jtnc.ar.tncc.session.timed.BatchEnding;
import de.hsbremen.jtnc.ar.tncc.session.timed.BeginHandshake;

public class StateDecided implements TnccsState {

    private TnccsBatch resultBatch;

    public StateDecided(TnccsBatch resultBatch) {
        this.resultBatch = resultBatch;
    }

    @Override
    public TnccsState handle(TnccsSession context) {
        // TODO Auto-generated method stub
        TnccsSession session = context;
        if (this.resultBatch != null) {

            try {
                TnccsState next = this.handleBatchType(session);
                return next;
            } catch (TNCException e) {
                // If anything happens go to end state
                // TODO Auto-generated method stub
                // sendCloseBatch
                // transition to TnccEndState
                return (TnccsState) new StateEnd();
            }

        }else {
            this.resultBatch = session.receive();
            return this;
        }
    }

    private TnccsState handleBatchType(TnccsSession session) throws TNCException {

        TnccsHeaderValidatorFactory.createTnccsIetfServerBatchValidatorV2().validate(this.resultBatch);

        // TODO what about server retry?
        switch (this.resultBatch.getType()) {
        case RESULT:
            // Work thru messages
            this.deliverMessages(this.resultBatch.getMessages(), session);
            // Messages are send after the batch ending is informed
            this.informBatchEnding(session);
            // Dont to a transition wait for another message
            this.resultBatch = null;

            return this;
        case CRETRY:
         // begin handshake
            TnccsBatch batch = this.beginHandshake(session);
            // send batch
            session.send(batch);
            // transition to ServerWorking state
            return new StateServerWorking(); 
            
        case CLOSE:
            // Transition to TnccEnd State
            return new StateEnd(this.resultBatch);
            
            // Not used here, because of RFC5793 p. 70 B.2. Eval against Req. C-2
            // case SRETRY:
            // Transition to TnccServerWorking State
            // return new TnccServerWorkingState();
        default:
            throw new TNCException("The batch type " + this.resultBatch.getType().toString()
                    + " is not valid in 'Server Working' state.", TNCException.TNC_RESULT_INVALID_PARAMETER);
        }
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
                // Purge all new messages because nothing sould be send here
                ((ImcMessageBuffer) connection).clearBuffer();
            }
        }
    }
    private TnccsBatch beginHandshake(TnccsSession session){
        // Send Client Batch and wait
        TnccsBatch batch = createClientBatch();
        
        Map<Long, IMCConnection> sessionConnections = session.getSessionImcConnections();
        List<ImcContainer> imcList = session.getKnownImc();
        
        for (ImcContainer imcContainer : imcList) {
            if(sessionConnections.containsKey(imcContainer.getPrimaryId())){
                
                IMCConnection connection = sessionConnections.get(imcContainer.getPrimaryId());
                //Use executor to execute a command and stop it after a fixed time length.
                try {
                    imcContainer.getImc().notifyConnectionChange(connection,
                            TNCConstants.TNC_CONNECTION_STATE_CREATE);
                } catch (TNCException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                AbstractTimedImcFunctionCall beginHandshake = new BeginHandshake(imcContainer, connection);
                beginHandshake.callFunction();
               
                this.addImMessagesToTnccsBatch(connection, batch);
            }
        }
       return batch;
    }
    
    private void addImMessagesToTnccsBatch(IMCConnection connection, TnccsBatch batch){
        if(connection instanceof ImcMessageBuffer){
            List<TnccsMessageIm> messages = ((ImcMessageBuffer)connection).getBuffer();
            for (TnccsMessageIm message : messages) {
                batch.getMessages().add(message);
            }
            ((ImcMessageBuffer)connection).clearBuffer();
        }else{
            // If this happens something went clearly wrong, so throw it up to the top.
            throw new ImcMessageBufferMandatoryException("An IMC connection class must extend the ImcMessageBuffer class.");
        }
    }
    
    private TnccsBatch createClientBatch(){
        TnccsBatch batch = new TnccsBatch();
        batch.setMessages(new ArrayList<TnccsAbstractMessage>());
        batch.setDirectionality(TnccsBatchDirectionalityEnum.TO_PBS);
        batch.setType(TnccsBatchTypeEnum.CDATA);
        return batch;
    }
}
