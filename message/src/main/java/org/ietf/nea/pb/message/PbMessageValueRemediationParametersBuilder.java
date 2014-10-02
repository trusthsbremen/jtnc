package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueRemediationParametersBuilder extends TnccsMessageValueBuilder {

	/**
	 * @param rpVendorId the rpVendorId to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueRemediationParametersBuilder setRpVendorId(long rpVendorId)
			throws RuleException;

	/**
	 * @param rpType the rpType to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueRemediationParametersBuilder setRpType(long rpType) throws RuleException;

	/**
	 * @param parameter the parameter to set
	 */
	public abstract PbMessageValueRemediationParametersBuilder setParameter(
			AbstractPbMessageSubValue parameter);

}