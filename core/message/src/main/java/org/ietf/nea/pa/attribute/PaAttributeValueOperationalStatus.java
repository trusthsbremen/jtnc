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
package org.ietf.nea.pa.attribute;

import java.util.Date;

import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;

/**
 * IETF RFC 5792 integrity measurement operational status attribute value.
 *
 *
 */
public class PaAttributeValueOperationalStatus extends
    AbstractPaAttributeValue {

    private final PaAttributeOperationStatusEnum status; // 8 bit(s)
    private final PaAttributeOperationLastResultEnum result; // 8 bit(s)
    private final Date lastUse; // 160 bit(s) RFC 3339 formatted
                                // YYYY-MM-DDTHH:MM:SSZ

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param status the operational status
     * @param result the result of the last use
     * @param lastUse the date of the last use
     */
    PaAttributeValueOperationalStatus(final long length,
            final PaAttributeOperationStatusEnum status,
            final PaAttributeOperationLastResultEnum result,
            final Date lastUse) {
        super(length);
        this.status = status;
        this.result = result;
        this.lastUse = lastUse;
    }

    /**
     * Returns the operational status.
     * @return the status
     */
    public PaAttributeOperationStatusEnum getStatus() {
        return this.status;
    }

    /**
     * Returns the result of the last use.
     * @return the result
     */
    public PaAttributeOperationLastResultEnum getResult() {
        return this.result;
    }

    /**
     * Returns the date of the last use.
     * @return the last use date
     */
    public Date getLastUse() {
        return this.lastUse;
    }

    @Override
    public String toString() {
        return "PaAttributeValueOperationalStatus [status="
                + this.status.toString() + ", result="
                + this.result.toString() + ", lastUse="
                + this.lastUse.toString() + "]";
    }

}
