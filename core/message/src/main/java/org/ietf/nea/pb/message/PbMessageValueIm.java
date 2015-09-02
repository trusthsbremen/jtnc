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

import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;

/**
 * IETF RFC 5793 TNCCS integrity measurement component message value.
 *
 *
 */
public class PbMessageValueIm extends AbstractPbMessageValue {

    private static final boolean OMMITTABLE = Boolean.FALSE;

    private final EnumSet<PbMessageImFlagEnum> imFlags; // 8 bit(s)

    private final long subVendorId; // 24 bit(s)
    private final long subType; // 32 bit(s)
    private final long collectorId; // 16 bit(s)
    private final long validatorId; // 16 bit(s)

    private final byte[] message; // PA message as byte[]

    /**
     * Creates the message value with the given values.
     *
     * @param flags the integrity measurement component flags
     * @param subVendorId the component vendor ID
     * @param subType the type describing the component
     * @param collectorId the IMC ID receiving/sending
     * the measurements
     * @param validatorId the IMV ID receiving/sending
     * the measurements
     * @param length the value length
     * @param message the integrity measurement component message
     */
    PbMessageValueIm(final PbMessageImFlagEnum[] flags, final long subVendorId,
            final long subType, final long collectorId, final long validatorId,
            final long length, final byte[] message) {
        super(length, OMMITTABLE);

        if (flags != null && flags.length > 0) {
            this.imFlags = EnumSet.copyOf(Arrays.asList(flags));
        } else {
            this.imFlags = EnumSet.noneOf(PbMessageImFlagEnum.class);
        }
        this.subVendorId = subVendorId;
        this.subType = subType;
        this.collectorId = collectorId;
        this.validatorId = validatorId;
        this.message = (message != null) ? message : new byte[0];
    }

    /**
     * Returns the set of used integrity measurement component flags.
     * @return the component flags
     */
    public Set<PbMessageImFlagEnum> getImFlags() {
        return Collections.unmodifiableSet(imFlags);
    }

    /**
     * Returns the component vendor ID.
     * @return the component vendor ID
     */
    public long getSubVendorId() {
        return subVendorId;
    }

    /**
     * Returns the  component type ID.
     * @return the component type ID
     */
    public long getSubType() {
        return subType;
    }

    /**
     * Returns the receiving/sending IMC ID.
     * @return the IMC ID
     */
    public long getCollectorId() {
        return collectorId;
    }

    /**
     * Returns the receiving/sending IMV ID.
     * @return the IMV ID
     */
    public long getValidatorId() {
        return validatorId;
    }

    /**
     * Returns the integrity measurement component message.
     * @return the message
     */
    public byte[] getMessage() {
        return Arrays.copyOf(this.message, this.message.length);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PbMessageValueIm [imFlags="
                + Arrays.toString(
                        this.imFlags.toArray(new PbMessageImFlagEnum[0]))
                + ", subVendorId=" + this.subVendorId
                + ", subType=" + this.subType
                + ", collectorId=" + this.collectorId + ", validatorId="
                + this.validatorId + ", message=ommitted size:"
                + this.message.length + "]";
    }

}
