package org.ietf.nea.pa.attribute.util;

import java.nio.charset.Charset;


public class PackageEntry {

    private final byte packageNameLength;          // 8 bit(s) length of the string in octets
    private final String packageName;              // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
    private final byte packageVersionLength;       // 8 bit(s) length of language code in octets
    private final String packageVersion;           // variable length, UTF-8 encoded, NUL termination MUST NOT be included.
	
    public PackageEntry(String packageName, String packageVersion) {
		super();
		this.packageName = packageName;
		this.packageNameLength = (byte)packageName.getBytes(Charset.forName("UTF-8")).length;
		this.packageVersion = packageVersion;
		this.packageVersionLength = (byte)packageVersion.getBytes(Charset.forName("UTF-8")).length;
	}

	/**
	 * @return the packageNameLength
	 */
	public byte getPackageNameLength() {
		return this.packageNameLength;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return this.packageName;
	}

	/**
	 * @return the packageVersionLength
	 */
	public byte getPackageVersionLength() {
		return this.packageVersionLength;
	}

	/**
	 * @return the packageVersion
	 */
	public String getPackageVersion() {
		return this.packageVersion;
	}
	
}
