package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueProductInformationBuilder extends ImAttributeValueBuilder{
	
	/**
	 * @param vendorId the vendorId to set
	 * @throws RuleException 
	 */
	public abstract void setVendorId(long vendorId) throws RuleException;

	/**
	 * @param productId the productId to set
	 * @throws RuleException 
	 */
	public abstract void setProductId(int productId) throws RuleException;

	/**
	 * @param name the name to set
	 */
	public abstract void setName(String name) throws RuleException;

}
