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
package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

/**
 * Generic builder to build a TNCCS error message value compliant to RFC 5793.
 * It can be used in a fluent way.
 *
 *
 */
public interface PbMessageValueErrorBuilder extends TnccsMessageValueBuilder {

    /**
     * Sets the error flags encoded as byte.
     *
     * @param errorFlags the error flags
     * @return this builder
     */
    PbMessageValueErrorBuilder setErrorFlags(byte errorFlags);

    /**
     * Sets the error vendor ID.
     *
     * @param errorVendorId the error vendor ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueErrorBuilder setErrorVendorId(long errorVendorId)
            throws RuleException;

    /**
     * Sets the code describing the error.
     *
     * @param errorCode the error code
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageValueErrorBuilder setErrorCode(int errorCode) throws RuleException;

    /**
     * Sets the additional error parameter.
     *
     * @param errorParameter the error parameter
     * @return this builder
     */
    PbMessageValueErrorBuilder setErrorParameter(
            AbstractPbMessageValueErrorParameter errorParameter);

}
