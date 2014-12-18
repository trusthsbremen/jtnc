package de.hsbremen.tc.tnc.transport;



public interface TransportConnectionBuilder {

	public abstract TransportConnection toConnection(boolean selfInitiated, boolean server,
			Object underlying);
}
