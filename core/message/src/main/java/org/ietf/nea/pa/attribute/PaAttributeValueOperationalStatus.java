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
package org.ietf.nea.pa.attribute;

import java.util.Date;

import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;

/**
 * IETF RFC 5792 integrity measurement operational status attribute value.
 *
 * @author Carl-Heinz Genzel
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
