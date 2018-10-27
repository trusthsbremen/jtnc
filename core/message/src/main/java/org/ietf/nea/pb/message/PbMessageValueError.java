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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;

/**
 * IETF RFC 5793 TNCCS error message value.
 *
 *
 */
public class PbMessageValueError extends AbstractPbMessageValue {

    private static final boolean OMMITTABLE = Boolean.FALSE;

    private final EnumSet<PbMessageErrorFlagsEnum> errorFlags; // 8 bit(s)
    private final long errorVendorId; // 24 bit(s)
    private final int errorCode; // 16 bit(s)
    private AbstractPbMessageValueErrorParameter errorParameter; // 32 bit(s)

    /**
     * Creates the message value with the given values.
     *
     * @param flags the error flags
     * @param errorVendorId the error vendor ID
     * @param errorCode the code describing the error
     * @param length the value length
     * @param errorParameter the additional error parameter
     */
    PbMessageValueError(final PbMessageErrorFlagsEnum[] flags,
            final long errorVendorId, final int errorCode, final long length,
            final AbstractPbMessageValueErrorParameter errorParameter) {
        super(length, OMMITTABLE);

        if (flags != null && flags.length > 0) {
            this.errorFlags = EnumSet.copyOf(Arrays.asList(flags));
        } else {
            this.errorFlags = EnumSet.noneOf(PbMessageErrorFlagsEnum.class);
        }

        this.errorVendorId = errorVendorId;
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }

    /**
     * Returns the set of used error flags.
     * @return the set of error flags
     */
    public Set<PbMessageErrorFlagsEnum> getErrorFlags() {
        return Collections.unmodifiableSet(this.errorFlags);
    }

    /**
     * Returns the error vendor ID.
     * @return the error vendor ID
     */
    public long getErrorVendorId() {
        return this.errorVendorId;
    }

    /**
     * Returns the error code.
     * @return the error code
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the the additional error parameter.
     * @return the error parameter
     */
    public AbstractPbMessageValueErrorParameter getErrorParameter() {

        return this.errorParameter;
    }

    @Override
    public String toString() {
        return "PbMessageValueError [errorFlags="
                + Arrays.toString(
                        this.errorFlags.toArray(new PbMessageErrorFlagsEnum[0]))
                + ", errorVendorId=" + this.errorVendorId + ", errorCode="
                + this.errorCode + ", errorParameter=" +((this.errorParameter != null) ?
                this.errorParameter.toString() : "null") + "]";
    }
}
