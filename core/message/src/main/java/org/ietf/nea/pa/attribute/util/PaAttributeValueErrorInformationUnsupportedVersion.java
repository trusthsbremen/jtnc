package org.ietf.nea.pa.attribute.util;


public class PaAttributeValueErrorInformationUnsupportedVersion extends AbstractPaAttributeValueErrorInformation{
  
    private final short maxVersion;
    private final short minVersion;   
    
	PaAttributeValueErrorInformationUnsupportedVersion(final long length, final RawMessageHeader messageHeader, final short maxVersion, final short minVersion) {
		super(length, messageHeader);
	
		this.maxVersion = maxVersion;
		this.minVersion = minVersion;
	}

	/**
	 * @return the maxVersion
	 */
	public short getMaxVersion() {
		return this.maxVersion;
	}

	/**
	 * @return the minVersion
	 */
	public short getMinVersion() {
		return this.minVersion;
	}

	
}
