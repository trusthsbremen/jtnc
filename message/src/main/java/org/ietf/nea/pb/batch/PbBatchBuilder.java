package org.ietf.nea.pb.batch;

import java.util.List;

import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatchBuilder;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface PbBatchBuilder extends TnccsBatchBuilder {

	public abstract PbBatchBuilder setVersion(byte version) throws ValidationException;
	
	public abstract PbBatchBuilder setDirection(byte direction) throws ValidationException;

	public abstract PbBatchBuilder setType(byte type) throws ValidationException;

	public abstract PbBatchBuilder addMessage(PbMessage message);

	public abstract PbBatchBuilder addMessages(List<PbMessage> messages);

}