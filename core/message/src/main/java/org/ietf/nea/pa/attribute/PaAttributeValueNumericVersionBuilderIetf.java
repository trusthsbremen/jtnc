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

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

/**
 * Builder to build an integrity measurement numeric version attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueNumericVersionBuilderIetf implements
        PaAttributeValueNumericVersionBuilder {

    private long length;
    private long majorVersion;
    private long minorVersion;
    private long buildVersion;
    private int servicePackMajor;
    private int servicePackMinor;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Major version: 0</li>
     * <li>Minor version: 0</li>
     * <li>Build version: 0</li>
     * <li>Service pack major: 0</li>
     * <li>Service pack minor: 0</lI>
     * </ul>
     */
    public PaAttributeValueNumericVersionBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.NUM_VER.length();
        this.majorVersion = 0L;
        this.minorVersion = 0L;
        this.buildVersion = 0L;
        this.servicePackMajor = 0;
        this.servicePackMinor = 0;
    }

    @Override
    public PaAttributeValueNumericVersionBuilder setMajorVersion(
            final long majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    @Override
    public PaAttributeValueNumericVersionBuilder setMinorVersion(
            final long minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    @Override
    public PaAttributeValueNumericVersionBuilder setBuildVersion(
            final long buildVersion) {
        this.buildVersion = buildVersion;
        return this;
    }

    @Override
    public PaAttributeValueNumericVersionBuilder setServicePackMajor(
            final int servicePackMajor) {
        this.servicePackMajor = servicePackMajor;
        return this;
    }

    @Override
    public PaAttributeValueNumericVersionBuilder setServicePackMinor(
            final int servicePackMinor) {
        this.servicePackMinor = servicePackMinor;
        return this;
    }

    @Override
    public PaAttributeValueNumericVersion toObject() {

        return new PaAttributeValueNumericVersion(this.length,
                this.majorVersion, this.minorVersion, this.buildVersion,
                this.servicePackMajor, this.servicePackMinor);
    }

    @Override
    public PaAttributeValueNumericVersionBuilder newInstance() {
        return new PaAttributeValueNumericVersionBuilderIetf();
    }

}
