/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;

/**
 * IETF RFC 5792 integrity measurement string version attribute value.
 *
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
