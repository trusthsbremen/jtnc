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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;
import org.ietf.nea.pa.validate.rules.CommonRfc3339TimeStampSyntaxCheck;

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
            CommonRfc3339TimeStampSyntaxCheck.check(dateTime);
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
