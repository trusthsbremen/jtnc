package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;

public interface PaAttributeValueErrorInformationUnsupportedAttributeBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(MessageHeaderDump messageHeader);

	public abstract void setAttributeHeader(PaAttributeHeader attributeHeader);
}
