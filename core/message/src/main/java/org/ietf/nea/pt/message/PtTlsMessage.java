package org.ietf.nea.pt.message;

import org.ietf.nea.pt.value.PtTlsMessageValue;

import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.util.NotNull;

public class PtTlsMessage implements TransportMessage{

	private final PtTlsMessageHeader header;
	private final PtTlsMessageValue value;

	public PtTlsMessage(PtTlsMessageHeader header, PtTlsMessageValue value) {
		NotNull.check("Message header cannot be null.", header);
		NotNull.check("Value cannot be null.", value);
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
