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
package org.ietf.nea.pb.message.util;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.IETFConstants;

/**
 * Factory utility to create an IETF RFC 5793 compliant error parameter.
 *
 *
 */
public final class PbMessageValueErrorParameterFactoryIetf {

    /**
     * Private constructor should never be invoked.
     */
    private PbMessageValueErrorParameterFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates an error parameter containing information where
     * the error occurred in another message.
     * @param errorVendorId the vendor ID of the error
     * @param errorCode the code describing the error
     * @param offset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5793 compliant error parameter
     */
    public static PbMessageValueErrorParameterOffset createErrorParameterOffset(
            final long errorVendorId, final long errorCode, final long offset) {

        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative.");
        }

        if (errorVendorId == IETFConstants.IETF_PEN_VENDORID
                && (errorCode
                        == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code()
                    || errorCode
                        == PbMessageErrorCodeEnum
                            .IETF_UNSUPPORTED_MANDATORY_MESSAGE.code())) {

            PbMessageValueErrorParameterOffset parameter =
                    new PbMessageValueErrorParameterOffset(
                    PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length(), offset);

            return parameter;
        }

        throw new IllegalArgumentException(
                "Requested error value is not supported in "
                        + "attribute with error vendor ID "
                        + errorVendorId + " and with error code " + errorCode
                        + ".");

    }

    /**
     * Creates an error parameter containing information about the
     * supported version.
     * @param badVersion the unsupported version
     * @param maxVersion the maximum supported version
     * @param minVersion the minimum supported version
     * @return an IETF RFC 5793 compliant error parameter
     */
    public static PbMessageValueErrorParameterVersion
        createErrorParameterVersion(
            final short badVersion,
            final short maxVersion, final short minVersion) {

        if (maxVersion < 0 || minVersion < 0) {
            throw new IllegalArgumentException(
                    "Version information cannot be negative.");
        }

        PbMessageValueErrorParameterVersion parameter =
                new PbMessageValueErrorParameterVersion(
                PbMessageTlvFixedLengthEnum.ERR_SUB_VALUE.length(),
                badVersion, maxVersion, minVersion);
        return parameter;
    }

}
