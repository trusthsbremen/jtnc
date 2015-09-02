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
package org.ietf.nea.pb.message;

/**
 * Generic IETF RFC 5793 TNCCS message value base. Especially important for
 * inheritance.
 *
 *
 */
public abstract class AbstractPbMessageValue implements PbMessageValue {

    private final long length;
    private final boolean omittable;

    /**
     * Creates the TNCCS message value base with
     * the given value length, that is omittable.
     *
     * @param length the value length
     */
    protected AbstractPbMessageValue(final long length) {
        this(length, true);
    }

    /**
     * Creates the TNCCS message value base with
     * the given values.
     *
     * @param length the value length
     * @param omittable the importance of the message
     */
    protected AbstractPbMessageValue(
            final long length, final boolean omittable) {
        this.length = length;
        this.omittable = omittable;
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public boolean isOmittable() {
        return omittable;
    }

    @Override
    public String toString() {
        return "PbMessageValue [length=" + this.length + ", omittable="
                + this.omittable + "] (content omitted)";
    }
}
