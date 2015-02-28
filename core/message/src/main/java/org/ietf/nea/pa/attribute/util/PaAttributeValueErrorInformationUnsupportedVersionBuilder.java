package org.ietf.nea.pa.attribute.util;


public interface PaAttributeValueErrorInformationUnsupportedVersionBuilder extends PaAttributeValueErrorInformationBuilder{
	
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public abstract void setMessageHeader(RawMessageHeader messageHeader);
	
	/**
	 * @param offset the offset to set
	 */
	public abstract void setMaxVersion(short maxVersion);

	/**
	 * @param offset the offset to set
	 */
	public abstract void setMinVersion(short minVersion);
}
