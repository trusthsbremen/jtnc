package de.hsbremen.tc.tnc.message.t.value;


public interface TransportMessageValueBuilder {

	public abstract TransportMessageValue toValue();

	public abstract TransportMessageValueBuilder clear();
}
