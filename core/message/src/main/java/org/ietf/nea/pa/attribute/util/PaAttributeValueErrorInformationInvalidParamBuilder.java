package org.ietf.nea.pa.attribute.util;


public interface PaAttributeValueErrorInformationInvalidParamBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(MessageHeaderDump messageHeader);
	

	/**
	 * @param offset the offset to set
	 */
	public abstract void setOffset(long offset);
}
