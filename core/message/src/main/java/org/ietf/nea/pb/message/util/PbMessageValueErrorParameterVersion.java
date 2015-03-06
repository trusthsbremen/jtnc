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
package org.ietf.nea.pb.message.util;

/**
 * IETF RFC 5793 TNCCS unsupported version error parameter.
 *
 *
 */
public class PbMessageValueErrorParameterVersion extends
        AbstractPbMessageValueErrorParameter {

    private final short badVersion; // 8 bit(s)
    private final short maxVersion; // 8 bit(s)
    private final short minVersion; // 8 bit(s)

    /**
     * Creates the error parameter with the given values.
     *
     * @param length the parameter length
     * @param badVersion the bad version
     * @param maxVersion the maximum supported version
     * @param minVersion the minimum supported version
     */
    public PbMessageValueErrorParameterVersion(final long length,
            final short badVersion,
            final short maxVersion, final short minVersion) {
        super(length);
        this.badVersion = badVersion;
        this.maxVersion = maxVersion;
        this.minVersion = minVersion;
    }

    /**
     * Returns the bad version.
     * @return the bad version
     */
    public short getBadVersion() {
        return this.badVersion;
    }

    /**
     * Returns the maximum supported version.
     * @return the maximum version
     */
    public short getMaxVersion() {
        return this.maxVersion;
    }

    /**
     * Returns the minimum supported version.
     * @return the minimum version
     */
    public short getMinVersion() {
        return this.minVersion;
    }
}
