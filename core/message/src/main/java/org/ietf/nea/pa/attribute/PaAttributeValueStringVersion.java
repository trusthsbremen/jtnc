package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;


public class PaAttributeValueStringVersion extends AbstractPaAttributeValue {

    private final short versionLength;           // 8 bit(s) length of the string in octets
    private final String versionNumber;         // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private final short buildLength;        		// 8 bit(s) length of language code in octets
    private final String buildVersion;          // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private final short configLength;        	// 8 bit(s) length of language code in octets
    private final String configurationVersion;  // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
	
    PaAttributeValueStringVersion(long length, String versionNumber,
			String buildVersion, String configurationVersion) {
		super(length);
		this.versionNumber = versionNumber;
		this.versionLength = (byte)versionNumber.getBytes(Charset.forName("UTF-8")).length;
		this.buildVersion = buildVersion;
		this.buildLength = (byte)buildVersion.getBytes(Charset.forName("UTF-8")).length;
		this.configurationVersion = configurationVersion;
		this.configLength = (byte)configurationVersion.getBytes(Charset.forName("UTF-8")).length;
	}

	/**
	 * @return the versionLength
	 */
	public short getVersionNumberLength() {
		return this.versionLength;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return this.versionNumber;
	}

	/**
	 * @return the buildLength
	 */
	public short getBuildVersionLength() {
		return this.buildLength;
	}

	/**
	 * @return the buildVersion
	 */
	public String getBuildVersion() {
		return this.buildVersion;
	}

	/**
	 * @return the configLength
	 */
	public short getConfigurationVersionLength() {
		return this.configLength;
	}

	/**
	 * @return the configurationVersion
	 */
	public String getConfigurationVersion() {
		return this.configurationVersion;
	}

	
    

	

	
}
