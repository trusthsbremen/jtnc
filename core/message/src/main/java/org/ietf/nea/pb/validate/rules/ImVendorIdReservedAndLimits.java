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
package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the integrity measurement component vendor ID
 * is reserved or not in range.
 *
 */
public final class ImVendorIdReservedAndLimits {
    /**
     * Private constructor should never be invoked.
     */
    private ImVendorIdReservedAndLimits() {
        throw new AssertionError();
    }
    /**
     * Checks if the the integrity measurement component vendor ID
     * is reserved or not in range.
     * @param vendorId the vendor ID
     * @throws RuleException if check fails
     */
    public static void check(final long vendorId) throws RuleException {
        if (vendorId == TNCConstants.TNC_VENDORID_ANY) {
            throw new RuleException("Sub Vendor ID is set to reserved value.",
                    true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PbErrorCauseEnum.SUB_VENDOR_ID_RESERVED.id(), vendorId);
        }

        if (vendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new RuleException("Subvendor ID is to large.", true,
                    PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PbErrorCauseEnum.VALUE_TO_LARGE.id(), vendorId);
        }

        if (vendorId < 0) {
            throw new RuleException("Subvendor ID cannot be negativ.", true,
                    PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PbErrorCauseEnum.NEGATIV_UNSIGNED.id(), vendorId);
        }

    }

}
