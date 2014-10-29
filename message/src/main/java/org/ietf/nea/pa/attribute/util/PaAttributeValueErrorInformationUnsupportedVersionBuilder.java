package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.message.PaMessageHeader;

public interface PaAttributeValueErrorInformationUnsupportedVersionBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(PaMessageHeader messageHeader);
	
	/**
	 * @param offset the offset to set
	 */
	public abstract void setMaxVersion(short maxVersion);

	/**
	 * @param offset the offset to set
	 */
	public abstract void setMinVersion(short minVersion);
}
