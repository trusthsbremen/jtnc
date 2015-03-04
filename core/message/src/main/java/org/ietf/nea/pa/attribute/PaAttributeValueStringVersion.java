/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;

/**
 * IETF RFC 5792 integrity measurement string version attribute value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueStringVersion extends AbstractPaAttributeValue {

    private final short versionLength; // 8 bit(s) length of the string in
                                       // octets
    private final String versionNumber; // variable length, UTF-8 encoded, NUL
                                        // termination MUST NOT be included.
    private final short buildLength; // 8 bit(s) length of language code in
                                     // octets
    private final String buildVersion; // variable length, UTF-8 encoded, NUL
                                       // termination MUST NOT be included.
    private final short configLength; // 8 bit(s) length of language code in
                                      // octets
    private final String configurationVersion; // variable length, UTF-8
                                               // encoded, NUL termination MUST
                                               // NOT be included.

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param versionNumber the product version number
     * @param buildVersion the build number
     * @param configurationVersion the configuration version
     */
    PaAttributeValueStringVersion(final long length, final String versionNumber,
            final String buildVersion, final String configurationVersion) {
        super(length);
        this.versionNumber = versionNumber;
        this.versionLength = (byte) versionNumber.getBytes(Charset
                .forName("UTF-8")).length;
        this.buildVersion = buildVersion;
        this.buildLength = (byte) buildVersion.getBytes(Charset
                .forName("UTF-8")).length;
        this.configurationVersion = configurationVersion;
        this.configLength = (byte) configurationVersion.getBytes(Charset
                .forName("UTF-8")).length;
    }

    /**
     * Returns the version number length.
     * @return the version number length
     */
    public short getVersionNumberLength() {
        return this.versionLength;
    }

    /**
     * Returns the version number.
     * @return the version number
     */
    public String getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Returns the build version length.
     * @return the build version length
     */
    public short getBuildVersionLength() {
        return this.buildLength;
    }

    /**
     * Returns the build version.
     * @return the build version
     */
    public String getBuildVersion() {
        return this.buildVersion;
    }

    /**
     * Returns the configuration version length.
     * @return the configuration version length
     */
    public short getConfigurationVersionLength() {
        return this.configLength;
    }

    /**
     * Returns the configuration version.
     * @return the configuration version
     */
    public String getConfigurationVersion() {
        return this.configurationVersion;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PaAttributeValueStringVersion [versionLength="
                + this.versionLength + ", versionNumber=" + this.versionNumber
                + ", buildLength=" + this.buildLength + ", buildVersion="
                + this.buildVersion + ", configLength=" + this.configLength
                + ", configurationVersion=" + this.configurationVersion + "]";
    }

}
