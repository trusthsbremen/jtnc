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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;
import org.ietf.nea.pa.validate.rules.LastUseSyntaxCheck;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement operational status attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueOperationalStatusBuilderIetf implements
        PaAttributeValueOperationalStatusBuilder {

    private long length;
    private PaAttributeOperationStatusEnum status;
    private PaAttributeOperationLastResultEnum result;
    private Date lastUse;
    private SimpleDateFormat dateFormater;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Status: Unknown</li>
     * <li>Result: Unknown</li>
     * <li>Last Use: null</li>
     * </ul>
     */
    public PaAttributeValueOperationalStatusBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.OP_STAT.length();
        this.status = PaAttributeOperationStatusEnum.UNKNOWN;
        this.result = PaAttributeOperationLastResultEnum.UNKNOWN;
        this.lastUse = null;

        this.dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public PaAttributeValueOperationalStatusBuilder setStatus(
            final short status) {
        // defaults to unknown
        this.status = PaAttributeOperationStatusEnum.fromId(status);
        return this;
    }

    @Override
    public PaAttributeValueOperationalStatusBuilder setResult(
            final short result) {
        // defaults to unknown
        this.result = PaAttributeOperationLastResultEnum.fromCode(result);
        return this;
    }

    @Override
    public PaAttributeValueOperationalStatusBuilder setLastUse(
            final String dateTime) throws RuleException {
        if (dateTime != null && !dateTime.equals("0000-00-00T00:00:00Z")) {
            LastUseSyntaxCheck.check(dateTime);
            try {
                Date d = this.dateFormater.parse(dateTime);
                this.lastUse = d;
            } catch (ParseException e) {
                // should never happen because of the check before.
                throw new RuleException("Time format: " + dateTime
                        + " could not be parsed.", e, false,
                        PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PaErrorCauseEnum.TIME_FORMAT_NOT_VALID.id(),
                        dateTime);
            }

        }
        return this;
    }

    @Override
    public PaAttributeValueOperationalStatus toObject() {

        return new PaAttributeValueOperationalStatus(this.length, this.status,
                this.result, this.lastUse);
    }

    @Override
    public PaAttributeValueOperationalStatusBuilder newInstance() {

        return new PaAttributeValueOperationalStatusBuilderIetf();
    }

}
