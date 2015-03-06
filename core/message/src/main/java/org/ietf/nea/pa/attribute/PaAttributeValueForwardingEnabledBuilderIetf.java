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

import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.ForwardingStatus;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement traffic forwarding status attribute
 * value compliant to RFC 5792. It evaluates the given values and can be used in
 * a fluent way.
 *
 *
 */
public class PaAttributeValueForwardingEnabledBuilderIetf implements
        PaAttributeValueForwardingEnabledBuilder {

    private long length;
    private PaAttributeForwardingStatusEnum status;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Status: Unknown</li>
     * </ul>
     */
    public PaAttributeValueForwardingEnabledBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.FWD_EN.length();
        this.status = PaAttributeForwardingStatusEnum.UNKNWON;
    }

    @Override
    public PaAttributeValueForwardingEnabledBuilder setStatus(final long status)
            throws RuleException {

        ForwardingStatus.check(status);
        this.status = PaAttributeForwardingStatusEnum.fromId(status);

        return this;
    }

    @Override
    public PaAttributeValueForwardingEnabled toObject() {

        return new PaAttributeValueForwardingEnabled(this.length, this.status);
    }

    @Override
    public PaAttributeValueForwardingEnabledBuilder newInstance() {
        // TODO Auto-generated method stub
        return new PaAttributeValueForwardingEnabledBuilderIetf();
    }

}
