package de.hsbremen.tc.tnc.message.t.message;


public interface TransportMessageHeaderBuilder {
	public abstract TransportMessageHeader toMessageHeader();

	public abstract TransportMessageHeaderBuilder clear();
}
