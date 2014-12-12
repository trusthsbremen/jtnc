package de.hsbremen.tc.tnc.message.t.message;


public interface TransportMessageHeaderBuilder {
	public abstract TransportMessageHeader toTransportHeader();

	public abstract TransportMessageHeaderBuilder clear();
}
