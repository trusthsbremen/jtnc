package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueErrorBuilder extends TnccsMessageValueBuilder {

	/**
	 * @param errorFlags the errorFlags to set
	 */
	public abstract void setErrorFlags(byte errorFlags);

	/**
	 * @param errorVendorId the errorVendorId to set
	 * @throws ValidationException 
	 */
	public abstract void setErrorVendorId(long errorVendorId)
			throws ValidationException;

	/**
	 * @param errorCode the errorCode to set
	 * @throws ValidationException 
	 */
	public abstract void setErrorCode(short errorCode)
			throws ValidationException;

	/**
	 * @param errorParameter the errorParameter to set
	 */
	public abstract void setErrorParameter(byte[] errorParameter);

}