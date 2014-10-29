package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueImBuilder extends TnccsMessageValueBuilder {

	/**
	 * @param imFlags the imFlags to set
	 */
	public abstract PbMessageValueImBuilder setImFlags(byte imFlags);

	/**
	 * @param subVendorId the subVendorId to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueImBuilder setSubVendorId(long subVendorId)
			throws RuleException;

	/**
	 * @param subType the subType to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueImBuilder setSubType(long subType) throws RuleException;

	/**
	 * @param collectorId the collectorId to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueImBuilder setCollectorId(long collectorId)
			throws RuleException;

	/**
	 * @param validatorId the validatorId to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueImBuilder setValidatorId(long validatorId)
			throws RuleException;

	/**
	 * @param message the message to set
	 */
	public abstract PbMessageValueImBuilder setMessage(byte[] message);

}