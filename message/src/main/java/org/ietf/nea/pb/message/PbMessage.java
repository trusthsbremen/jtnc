package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class PbMessage implements TnccsMessage{

	private final PbMessageHeader header;
	private final PbMessageValue value;

	public PbMessage(PbMessageHeader header, PbMessageValue value) {
		if(header == null){
			throw new NullPointerException("Message header cannot be null.");
		}
		if(value == null){
			throw new NullPointerException("Messages cannot be null.");
		}
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
