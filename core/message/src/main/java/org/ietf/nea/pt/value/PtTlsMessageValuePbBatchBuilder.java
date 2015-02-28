package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public interface PtTlsMessageValuePbBatchBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValuePbBatchBuilder setTnccsData(ByteBuffer data)
			throws RuleException;

}