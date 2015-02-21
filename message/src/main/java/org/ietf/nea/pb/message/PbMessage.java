package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.util.NotNull;

public class PbMessage implements TnccsMessage{

	private final PbMessageHeader header;
	private final PbMessageValue value;

	public PbMessage(PbMessageHeader header, PbMessageValue value) {
		NotNull.check("Message header cannot be null.", header);
		NotNull.check("Messages cannot be null.", value);
		this.header = header;
		this.value = value;
	}

	@Override
	public PbMessageHeader getHeader() {
		return this.header;
	}

	@Override
	public PbMessageValue getValue() {
		return this.value;
	}

}
