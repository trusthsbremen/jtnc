package org.ietf.nea.pt.message;

import org.ietf.nea.pt.value.PtTlsMessageValue;

import de.hsbremen.tc.tnc.message.t.message.TransportMessage;

public class PtTlsMessage implements TransportMessage{

	private final PtTlsMessageHeader header;
	private final PtTlsMessageValue value;

	public PtTlsMessage(PtTlsMessageHeader header, PtTlsMessageValue value) {
		if(header == null){
			throw new NullPointerException("Message header cannot be null.");
		}
		if(value == null){
			throw new NullPointerException("Value cannot be null.");
		}
		this.header = header;
		this.value = value;
	}

	@Override
	public PtTlsMessageHeader getHeader() {
		return this.header;
	}

	@Override
	public PtTlsMessageValue getValue() {
		return this.value;
	}
	
	
	
}
