/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pb.message.util;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.IETFConstants;

/**
 * Factory utility to create an IETF RFC 5793 compliant error parameter.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PbMessageValueErrorParameterFactoryIetf {

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
