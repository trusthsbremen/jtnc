package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.message.PaMessageHeader;

public interface PaAttributeValueErrorInformationInvalidParamBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(PaMessageHeader messageHeader);
	

	/**
	 * @param offset the offset to set
	 */
	public abstract void setOffset(long offset);
}
