package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageHeader;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class PbMessageNew implements TnccsMessage{

	private final PbMessageHeader header;
	private final PbMessageValue value;

	public PbMessageNew(PbMessageHeader header, PbMessageValue value) {
		super();
		this.header = header;
		this.value = value;
	}

	@Override
	public TnccsMessageHeader getHeader() {
		return this.header;
	}

	@Override
	public TnccsMessageValue getValue() {
		return this.value;
	}

}
