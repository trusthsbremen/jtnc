package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.RawMessageHeader;

public interface PaAttributeValueErrorInformationInvalidParamBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(RawMessageHeader messageHeader);
	

	/**
	 * @param offset the offset to set
	 */
	public abstract void setOffset(long offset);
}
