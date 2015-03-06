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

import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.ImIdLimits;
import org.ietf.nea.pb.validate.rules.ImMessageTypeReservedAndLimits;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS integrity measurement component message value
 * compliant to RFC 5793. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PbMessageValueImBuilderIetf implements PbMessageValueImBuilder {

    private PbMessageImFlagEnum[] imFlags; // 8 bit(s)

    private long subVendorId; // 24 bit(s)
    private long subType; // 32 bit(s)
    private long collectorId; // 16 bit(s)
    private long validatorId; // 16 bit(s)
    private long length;

    private byte[] message; // ImMessage as byte[]

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Flags: None set</li>
     * <li>Vendor: IETF</li>
     * <li>Type: Testing</li>
     * <li>IMC ID: Any</li>
     * <li>IMV ID: Any</li>
     * <li>Length: Fixed value length only</li>
     * <li>Message: Empty message</li>
     * </ul>
     */
    public PbMessageValueImBuilderIetf() {
        this.imFlags = new PbMessageImFlagEnum[0];
        this.subVendorId = IETFConstants.IETF_PEN_VENDORID;
        this.subType = 0;
        this.collectorId = TNCConstants.TNC_IMCID_ANY;
        this.validatorId = TNCConstants.TNC_IMVID_ANY;
        this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length();
        this.message = new byte[0];
    }

    @Override
    public PbMessageValueImBuilder setImFlags(final byte imFlags) {
        // filter for EXCL
        if ((byte) (imFlags & PbMessageImFlagEnum.EXCL.bit())
                == PbMessageImFlagEnum.EXCL.bit()) {
            this.imFlags = new PbMessageImFlagEnum[] {
                    PbMessageImFlagEnum.EXCL};
        }

        return this;
    }

    @Override
    public PbMessageValueImBuilder setSubVendorId(final long subVendorId)
            throws RuleException {

        ImMessageTypeReservedAndLimits.check(subVendorId);
        this.subVendorId = subVendorId;

        return this;
    }

    @Override
    public PbMessageValueImBuilder setSubType(final long subType)
            throws RuleException {

        ImMessageTypeReservedAndLimits.check(subType);
        this.subType = subType;

        return this;
    }

    @Override
    public PbMessageValueImBuilder setCollectorId(final long collectorId)
            throws RuleException {

        ImIdLimits.check(collectorId);
        this.collectorId = collectorId;

        return this;
    }

    @Override
    public PbMessageValueImBuilder setValidatorId(final long validatorId)
            throws RuleException {

        ImIdLimits.check(validatorId);
        this.validatorId = validatorId;

        return this;
    }

    @Override
    public PbMessageValueImBuilder setMessage(final byte[] message) {

        if (message != null) {
            this.message = message;
            this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length()
                    + message.length;
        }

        return this;
    }

    @Override
    public PbMessageValueIm toObject() {

        return new PbMessageValueIm(this.imFlags, this.subVendorId,
                this.subType, this.collectorId, this.validatorId, this.length,
                this.message);
    }

    @Override
    public PbMessageValueImBuilder newInstance() {

        return new PbMessageValueImBuilderIetf();
    }

}
