package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ServerWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

/**
 * Default TNCS client working state. The TNCS listens
 * for further messages in the client working state and changes
 * the state according to a newly received message.
 *
 * @author Carl-Heinz Genzel
 *
 */
class DefaultServerClientWorkingState extends AbstractState implements
        ServerWorking {

    private final StateHelper<TncsContentHandler> helper;

    /**
     * Creates the state with the given state helper.
     *
     * @param helper the state helper
     */
    DefaultServerClientWorkingState(
            final StateHelper<TncsContentHandler> helper) {

        this.helper = helper;
    }

    @Override
    public State getProcessorState(final TnccsBatch result) {

        if (result != null && result instanceof PbBatch) {
            PbBatch b = (PbBatch) result;

            if (b.getHeader().getType().equals(PbBatchTypeEnum.CDATA)) {

                return this.helper.getState(TnccsStateEnum.SERVER_WORKING);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)) {

                return this.helper.getState(TnccsStateEnum.END);
            }

            if (b.getHeader().getType().equals(PbBatchTypeEnum.CRETRY)) {

                return this.helper.getState(TnccsStateEnum.SERVER_WORKING);
            }

        }

        return this.helper.getState(TnccsStateEnum.ERROR);

    }

}
