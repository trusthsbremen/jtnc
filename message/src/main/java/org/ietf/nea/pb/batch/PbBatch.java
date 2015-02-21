package org.ietf.nea.pb.batch;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.util.NotNull;

public class PbBatch implements TnccsBatch {
	
    private final PbBatchHeader header;                           
    private final List<PbMessage> messages;

	public PbBatch(final PbBatchHeader header, List<PbMessage> messages) {
		NotNull.check("Batch header cannot be null.", header);
		NotNull.check("Messages cannot be null.", messages);
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
