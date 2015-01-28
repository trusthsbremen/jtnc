package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.attribute.Attributed;

public interface TransportAttributes extends Attributed {

	/**
	 * @return the tId
	 */
	public abstract String getTransportId();
	
	/**
	 * @return the tVersion
	 */
	public abstract String getTransportVersion();

	/**
	 * @return the tProtocol
	 */
	public abstract String getTransportProtocol();

	/**
	 * @return the maxMessageLength
	 */
	public abstract long getMaxMessageLength();

	/**
	 * @return the maxMessageLengthPerIm
	 */
	public abstract long getMaxMessageLengthPerIm();

	/**
	 * @return the maxRoundTrips
	 */
	public abstract long getMaxRoundTrips();
}