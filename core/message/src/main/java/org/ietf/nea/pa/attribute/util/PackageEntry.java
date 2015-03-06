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
package org.ietf.nea.pa.attribute.util;

import java.nio.charset.Charset;

/**
 * Entry object describing an installed package.
 *
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
