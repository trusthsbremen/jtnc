package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueErrorBuilder extends ImAttributeValueBuilder {

	/**
	 * @param rpVendorId the rpVendorId to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueErrorBuilder setErrorVendorId(long errorVendorId)
			throws RuleException;

	/**
	 * @param rpType the rpType to set
	 * @throws RuleException 
	 */
	public abstract PaAttributeValueErrorBuilder setErrorCode(long code) throws RuleException;

	/**
	 * @param parameter the parameter to set
	 */
	public abstract PaAttributeValueErrorBuilder setErrorInformation(
			AbstractPaAttributeValueErrorInformation errorInformation);

}