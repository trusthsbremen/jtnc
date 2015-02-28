package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueErrorBuilder extends TransportMessageValueBuilder {

	/**
	 * @param errorVendorId the errorVendorId to set
	 * @throws RuleException 
	 */
	public abstract PtTlsMessageValueErrorBuilder setErrorVendorId(long errorVendorId)
			throws RuleException;

	/**
	 * @param errorCode the errorCode to set
	 * @throws RuleException 
	 */
	public abstract PtTlsMessageValueErrorBuilder setErrorCode(long errorCode)
			throws RuleException;

	/**
	 * @param message the partial copy of the faulty message
	 */
	public abstract PtTlsMessageValueErrorBuilder setPartialMessage(byte[] message);

}