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
package org.ietf.nea.pa.attribute.util;

import java.util.Arrays;

/**
 * Helper object, that contains a parsed IETF RFC 5792 message header dump
 * including any reserved value fields for error handling.
 *
 *
 */
public class MessageHeaderDump {

    private final short version;
    private final byte[] reserved;
    private final long identifier;

    /**
     * Creates the helper object with the given message header values.
     * @param version the message format version
     * @param reserved the reserved message header part
     * @param identifier the message identifier
     */
    public MessageHeaderDump(final short version, final byte[] reserved,
            final long identifier) {
        this.version = version;
        this.reserved = (reserved != null) ? reserved : new byte[0];
        this.identifier = identifier;
    }

    /**
     * Returns the message format version.
     * @return the message format version
     */
    public short getVersion() {
        return this.version;
    }

    /**
     * Returns the reserved header part.
     * @return the reserved part
     */
    public byte[] getReserved() {
        return Arrays.copyOf(this.reserved, this.reserved.length);
    }

    /**
     * Returns the message identifier.
     * @return the identifier
     */
    public long getIdentifier() {
        return this.identifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (int) (this.identifier ^ (this.identifier >>> 32));
        result = prime * result + Arrays.hashCode(this.reserved);
        result = prime * result + this.version;
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
        MessageHeaderDump other = (MessageHeaderDump) obj;
        if (this.identifier != other.identifier) {
            return false;
        }
        if (!Arrays.equals(this.reserved, other.reserved)) {
            return false;
        }
        if (this.version != other.version) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RawMessageHeader [version=" + this.version + ", reserved="
                + Arrays.toString(this.reserved) + ", identifier="
                + this.identifier + "]";
    }

}
