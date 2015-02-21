package de.hsbremen.tc.tnc.message.util;


public interface TransmissionObjectBuilder<T> {

	public abstract T toObject();

	public abstract TransmissionObjectBuilder<T> newInstance();
}
