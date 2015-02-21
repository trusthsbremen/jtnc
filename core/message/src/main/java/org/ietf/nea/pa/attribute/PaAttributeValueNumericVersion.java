package org.ietf.nea.pa.attribute;


public class PaAttributeValueNumericVersion extends AbstractPaAttributeValue {
	
	private final long major; // 32 bit(s)
	private final long minor; // 32 bit(s)
	private final long build; // 32 bit(s)
	private final int servicePackMajor; // 16 bit(s)
	private final int servicePackMinor; // 16 bit(s)
	
	PaAttributeValueNumericVersion(long length, long majorVersion, long minorVersion,
			long buildVersion, int servicePackMajor, int servicePackMinor) {
		super(length);
		this.major = majorVersion;
		this.minor = minorVersion;
		this.build = buildVersion;
		this.servicePackMajor = servicePackMajor;
		this.servicePackMinor = servicePackMinor;
	}

	/**
	 * @return the major
	 */
	public long getMajorVersion() {
		return this.major;
	}

	/**
	 * @return the minor
	 */
	public long getMinorVersion() {
		return this.minor;
	}

	/**
	 * @return the build
	 */
	public long getBuildVersion() {
		return this.build;
	}

	/**
	 * @return the servicePackMajor
	 */
	public int getServicePackMajor() {
		return this.servicePackMajor;
	}

	/**
	 * @return the servicePackMinor
	 */
	public int getServicePackMinor() {
		return this.servicePackMinor;
	}

	

	
}
