package org.ietf.nea.pb.batch;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;

public class PbBatch implements TnccsBatch {
	
    private final PbBatchHeader header;                           
    private final List<PbMessage> messages;

	public PbBatch(final PbBatchHeader header, List<PbMessage> messages) {
		if(header == null){
			throw new NullPointerException("Batch header cannot be null.");
		}
		if(messages == null){
			throw new NullPointerException("Messages cannot be null.");
		}
		this.header = header;
		this.messages = messages;
	}

    /**
     * @return the messages
     */
	@Override
    public List<PbMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

	@Override
	public PbBatchHeader getHeader() {
		return this.header;
	}

}
