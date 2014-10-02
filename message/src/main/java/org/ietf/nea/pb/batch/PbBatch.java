package org.ietf.nea.pb.batch;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageNew;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatchHeader;

public class PbBatch implements TnccsBatch {
	
    private final PbBatchHeader header;                           // 32 bit(s) min value is 8 for the 8 bytes in this header
    private final List<PbMessageNew> messages;

	public PbBatch(final PbBatchHeader header, List<PbMessageNew> messages) {
		this.header = header;
		this.messages = messages;
	}

    /**
     * @return the messages
     */
	@Override
    public List<PbMessageNew> getMessages() {
        return Collections.unmodifiableList(messages);
    }

	@Override
	public TnccsBatchHeader getHeader() {
		return this.header;
	}

}
