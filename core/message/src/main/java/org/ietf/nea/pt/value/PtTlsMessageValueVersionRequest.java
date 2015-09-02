/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pt.value;

/**
 * IETF RFC 6876 transport version request message value.
 *
 *
 */
public class PtTlsMessageValueVersionRequest extends AbstractPtTlsMessageValue {

    private final short minVersion; // 8 bit(s)
    private final short maxVersion; // 8 bit(s)
    private final short preferredVersion; // 8 bit(s)

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param minVersion the minimum supported version
     * @param maxVersion the maximum supported version
     * @param preferredVersion the preferred version
     */
    PtTlsMessageValueVersionRequest(final long length, final short minVersion,
            final short maxVersion, final short preferredVersion) {
        super(length);
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.preferredVersion = preferredVersion;
    }

    /**
     * Returns the minimum supported version.
     *
     * @return the minimum version
     */
    public short getMinVersion() {
        return this.minVersion;
    }

    /**
     * Returns the the maximum supported version.
     *
     * @return the maximum version
     */
    public short getMaxVersion() {
        return this.maxVersion;
    }

    /**
     * Returns the preferred version.
     *
     * @return the preferred version
     */
    public short getPreferredVersion() {
        return this.preferredVersion;
    }
}
