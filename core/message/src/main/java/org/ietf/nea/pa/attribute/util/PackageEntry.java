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
package org.ietf.nea.pa.attribute.util;

import java.nio.charset.Charset;

/**
 * Entry object describing an installed package.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PackageEntry {

    /* 8 bit(s) length of the string in octets */
    private final short packageNameLength;
    /* variable length, UTF-8 encoded, NUL termination MUST NOT be included */
    private final String packageName;
    /* 8 bit(s) length of language code in octets */
    private final short packageVersionLength;
    /* variable length, UTF-8 encoded, NUL termination MUST NOT be included */
    private final String packageVersion;

    /**
     * Creates an entry object with the given package name and version.
     *
     * @param packageName the package name
     * @param packageVersion the package version
     */
    public PackageEntry(final String packageName, final String packageVersion) {
        super();
        this.packageName = packageName;
        this.packageNameLength = (byte) packageName.getBytes(Charset
                .forName("UTF-8")).length;
        this.packageVersion = packageVersion;
        this.packageVersionLength = (byte) packageVersion.getBytes(Charset
                .forName("UTF-8")).length;
    }

    /**
     * Returns the length of the package name.
     * @return the package name length
     */
    public short getPackageNameLength() {
        return this.packageNameLength;
    }

    /**
     * Returns the package name.
     * @return the package name
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     * Returns the length of the package version.
     * @return the package version length
     */
    public short getPackageVersionLength() {
        return this.packageVersionLength;
    }

    /**
     * Returns the package version.
     * @return the package version
     */
    public String getPackageVersion() {
        return this.packageVersion;
    }

    @Override
    public String toString() {
        return "PackageEntry [packageNameLength=" + this.packageNameLength
                + ", packageName=" + this.packageName
                + ", packageVersionLength=" + this.packageVersionLength
                + ", packageVersion=" + this.packageVersion + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.packageName == null) ? 0
                        : this.packageName.hashCode());
        result = prime * result + this.packageNameLength;
        result = prime
                * result
                + ((this.packageVersion == null) ? 0 : this.packageVersion
                        .hashCode());
        result = prime * result + this.packageVersionLength;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PackageEntry other = (PackageEntry) obj;
        if (this.packageName == null) {
            if (other.packageName != null) {
                return false;
            }
        } else if (!this.packageName.equals(other.packageName)) {
            return false;
        }
        if (this.packageNameLength != other.packageNameLength) {
            return false;
        }
        if (this.packageVersion == null) {
            if (other.packageVersion != null) {
                return false;
            }
        } else if (!this.packageVersion.equals(other.packageVersion)) {
            return false;
        }
        if (this.packageVersionLength != other.packageVersionLength) {
            return false;
        }
        return true;
    }
}
