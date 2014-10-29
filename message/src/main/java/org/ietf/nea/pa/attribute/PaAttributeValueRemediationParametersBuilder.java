package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueRemediationParameter;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueRemediationParametersBuilder extends ImAttributeValueBuilder {

	/**
	 * @param rpVendorId the rpVendorId to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueRemediationParametersBuilder setRpVendorId(long rpVendorId)
			throws RuleException;

	/**
	 * @param rpType the rpType to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueRemediationParametersBuilder setRpType(long rpType) throws RuleException;

	/**
	 * @param parameter the parameter to set
	 */
	public abstract PaAttributeValueRemediationParametersBuilder setParameter(
			AbstractPaAttributeValueRemediationParameter parameter);

}