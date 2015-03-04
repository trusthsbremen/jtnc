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

/**
 * IETF RFC 5792 integrity measurement numeric version attribute value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueNumericVersion extends AbstractPaAttributeValue {

    private final long major; // 32 bit(s)
    private final long minor; // 32 bit(s)
    private final long build; // 32 bit(s)
    private final int servicePackMajor; // 16 bit(s)
    private final int servicePackMinor; // 16 bit(s)

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param majorVersion the major product version
     * @param minorVersion the minor product version
     * @param buildVersion the build version
     * @param servicePackMajor the service pack major version
     * @param servicePackMinor the service pack major version
     */
    PaAttributeValueNumericVersion(final long length,
            final long majorVersion, final long minorVersion,
            final long buildVersion, final int servicePackMajor,
            final int servicePackMinor) {
        super(length);
        this.major = majorVersion;
        this.minor = minorVersion;
        this.build = buildVersion;
        this.servicePackMajor = servicePackMajor;
        this.servicePackMinor = servicePackMinor;
    }

    /**
     * Returns the major version.
     * @return the major version
     */
    public long getMajorVersion() {
        return this.major;
    }

    /**
     * Returns the minor version.
     * @return the minor version
     */
    public long getMinorVersion() {
        return this.minor;
    }

    /**
     * Returns the build version.
     * @return the build version
     */
    public long getBuildVersion() {
        return this.build;
    }

    /**
     * Returns the service pack major version.
     * @return the service pack major version
     */
    public int getServicePackMajor() {
        return this.servicePackMajor;
    }

    /**
     * Returns the service pack minor version.
     * @return the service pack minor version
     */
    public int getServicePackMinor() {
        return this.servicePackMinor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PaAttributeValueNumericVersion [major=" + this.major
                + ", minor=" + this.minor + ", build=" + this.build
                + ", servicePackMajor=" + this.servicePackMajor
                + ", servicePackMinor=" + this.servicePackMinor + "]";
    }
}
