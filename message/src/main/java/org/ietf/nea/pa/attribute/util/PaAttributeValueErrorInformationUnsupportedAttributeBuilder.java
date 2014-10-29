package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.message.PaMessageHeader;

public interface PaAttributeValueErrorInformationUnsupportedAttributeBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(PaMessageHeader messageHeader);

	public abstract void setAttributeHeader(PaAttributeHeader attributeHeader);
}
