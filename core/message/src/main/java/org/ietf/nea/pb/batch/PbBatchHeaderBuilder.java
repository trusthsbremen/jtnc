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
package org.ietf.nea.pb.batch;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatchHeaderBuilder;

/**
 * Generic builder to build a TNCCS batch header compliant to RFC 5793. It can
 * be used in a fluent way.
 *
 *
 */
public interface PbBatchHeaderBuilder extends TnccsBatchHeaderBuilder {

    /**
     * Sets the batch format version.
     *
     * @param version the batch version
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setVersion(short version) throws RuleException;

    /**
     * Sets the batch sending direction.
     *
     * @param direction the direction (e.g. to server or client)
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setDirection(byte direction) throws RuleException;

    /**
     * Sets the batch type.
     *
     * @param type the batch type (e.g. server data, result, ...)
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setType(byte type) throws RuleException;

    /**
     * Sets the batch length.
     *
     * @param length the batch length
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setLength(long length) throws RuleException;

}
