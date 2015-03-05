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
package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;

/**
 * IETF RFC 5793 TNCCS integrity measurement component message value.
 *
 * @author Carl-Heinz Genzel
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
