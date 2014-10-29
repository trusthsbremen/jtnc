package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.message.PaMessageHeader;

public class PaAttributeValueErrorInformationUnsupportedVersion extends AbstractPaAttributeValueErrorInformation{
  
    private final PaMessageHeader messageHeader; //variable length
    private final short maxVersion;
    private final short minVersion;   
    
	PaAttributeValueErrorInformationUnsupportedVersion(final long length, final PaMessageHeader messageHeader, final short maxVersion, final short minVersion) {
		super(length);
		this.messageHeader = messageHeader;
		this.maxVersion = maxVersion;
		this.minVersion = minVersion;
	}

	/**
	 * @return the messageHeader
	 */
	public PaMessageHeader getMessageHeader() {
		return this.messageHeader;
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
