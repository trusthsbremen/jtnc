package de.hsbremen.tc.tnc.im;

import java.util.Set;

import de.hsbremen.tc.tnc.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public interface Im<T extends ImAdapter<?>> {

	/**
	 * @return the attributes
	 */
	public abstract Attributed getAttributes();

	/**
	 * @return the imc
	 */
	public abstract T getIm();

	/**
	 * @return the supportedMessageTypes
	 */
	public abstract Set<SupportedMessageType> getSupportedMessageTypes();

	/**
	 * @param supportedMessageTypes the supportedMessageTypes to set
	 */
	public abstract void setSupportedMessageTypes(
			Set<SupportedMessageType> supportedMessageTypes);

	public abstract void terminate();

	public abstract boolean isTerminated();

	public abstract long getPrimaryId();

}