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

import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;

/**
 * IETF RFC 5793 TNCCS error message value.
 *
 * @author Carl-Heinz Genzel
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
                + this.errorCode + ", errorParameter="
                + this.errorParameter.toString() + "]";
    }
}
