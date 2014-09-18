package de.hsbremen.tc.tnc.session.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.tncc.IMCConnection;

import de.hsbremen.jtnc.ar.exception.tncc.ImcMessageBufferMandatoryException;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchDirectionalityEnum;
import de.hsbremen.jtnc.ar.message.enums.TnccsBatchTypeEnum;
import de.hsbremen.jtnc.ar.message.enums.TnccsMessageVendorIdAndTypeEnum;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsAbstractMessage;
import de.hsbremen.jtnc.ar.message.tnccs.TnccsBatch;
import de.hsbremen.jtnc.ar.message.tnccs.ietf.TnccsMessageIm;
import de.hsbremen.jtnc.ar.message.validator.TnccsHeaderValidatorFactory;
import de.hsbremen.jtnc.ar.tncc.imc.ImcContainer;
import de.hsbremen.jtnc.ar.tncc.imc.connection.ImcMessageBuffer;
import de.hsbremen.jtnc.ar.tncc.session.TnccsSession;
import de.hsbremen.jtnc.ar.tncc.session.timed.AbstractTimedImcFunctionCall;
import de.hsbremen.jtnc.ar.tncc.session.timed.BatchEnding;

public class StateClientWorking implements TnccsState {
    
    private TnccsBatch serverBatch;

    public StateClientWorking(TnccsBatch serverBatch) {
        this.serverBatch = serverBatch;
    }

    @Override
    public TnccsState handle(TnccsSession context) {
        // TODO Auto-generated method stub
        TnccsSession session = context;
        try {
            this.handleBatchType(session);
            return (TnccsState) new StateServerWorking();
        } catch (TNCException e) {
            // If anything happens go to end state
            // TODO Auto-generated method stub
            // sendCloseBatch
            // transition to TnccEndState
            return (TnccsState) new StateEnd();
        }
    }

    private TnccsState handleBatchType(TnccsSession session) throws TNCException {

        TnccsHeaderValidatorFactory.createTnccsIetfServerBatchValidatorV2().validate(serverBatch);

        // TODO what about server retry?
        switch (serverBatch.getType()) {
        case SDATA:
            // only result allows all message types so, use filter here
            this.filterMessageSequence(serverBatch);
            // Work thru messages
            this.deliverMessages(serverBatch.getMessages(), session);
            // Messages are send after the batch ending is informed
            this.informBatchEnding(session);
            // Transition to TnccServerWorkingState
            return new StateServerWorking();
        case CLOSE:
         // Transition to TnccServerWorkingState
            return new StateEnd(this.serverBatch);
        default:
            throw new TNCException("The batch type " + serverBatch.getType().toString()
                    + " is not valid in 'Server Working' state.", TNCException.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    private void filterMessageSequence(TnccsBatch batch) {

        // Iterator used here, because it is the only save way to remove elements from a list
        // while traversing it.
        Iterator<TnccsAbstractMessage> iter = batch.getMessages().iterator();
        while (iter.hasNext()) {
            TnccsAbstractMessage message = iter.next();

            // TODO may be add Experimental here to?
            // ignore/filter messages which should not be send within this batch type.
            if (message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()
                    || message.getType() == TnccsMessageVendorIdAndTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType()) {
                iter.remove();
            }
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

    
    private void informBatchEnding(TnccsSession session){

        TnccsBatch batch = createClientBatch();
        
        Map<Long, IMCConnection> sessionConnections = session.getSessionImcConnections();
        List<ImcContainer> imcList = session.getKnownImc();
        
        for (ImcContainer imcContainer : imcList) {
            if(sessionConnections.containsKey(imcContainer.getPrimaryId())){
                
                IMCConnection connection = sessionConnections.get(imcContainer.getPrimaryId());
                //Use executor to execute a command and stop it after a fixed time length.
                AbstractTimedImcFunctionCall batchEnding = new BatchEnding(imcContainer, connection);
                batchEnding.callFunction();
                
                this.addImMessagesToTnccsBatch(connection, batch);
            }
        }
        session.send(batch);
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
