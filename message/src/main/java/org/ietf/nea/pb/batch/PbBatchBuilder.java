package org.ietf.nea.pb.batch;

import java.util.List;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatchBuilder;

public interface PbBatchBuilder extends TnccsBatchBuilder {

	public abstract PbBatchBuilder setVersion(byte version) throws RuleException;
	
	public abstract PbBatchBuilder setDirection(byte direction) throws RuleException;

	public abstract PbBatchBuilder setType(byte type) throws RuleException;

	public abstract PbBatchBuilder addMessage(PbMessage message);

	public abstract PbBatchBuilder addMessages(List<PbMessage> messages);

}