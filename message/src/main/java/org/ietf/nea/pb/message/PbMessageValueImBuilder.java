package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueImBuilder extends TnccsMessageValueBuilder {

	/**
	 * @param imFlags the imFlags to set
	 */
	public abstract void setImFlags(byte imFlags);

	/**
	 * @param subVendorId the subVendorId to set
	 * @throws ValidationException 
	 */
	public abstract void setSubVendorId(long subVendorId)
			throws ValidationException;

	/**
	 * @param subType the subType to set
	 * @throws ValidationException 
	 */
	public abstract void setSubType(long subType) throws ValidationException;

	/**
	 * @param collectorId the collectorId to set
	 * @throws ValidationException 
	 */
	public abstract void setCollectorId(long collectorId)
			throws ValidationException;

	/**
	 * @param validatorId the validatorId to set
	 * @throws ValidationException 
	 */
	public abstract void setValidatorId(long validatorId)
			throws ValidationException;

	/**
	 * @param message the message to set
	 */
	public abstract void setMessage(byte[] message);

}