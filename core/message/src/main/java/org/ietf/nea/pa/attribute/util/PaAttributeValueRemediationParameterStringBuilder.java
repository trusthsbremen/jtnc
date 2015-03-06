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
package org.ietf.nea.pa.attribute.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Generic builder to build an integrity measurement string remediation
 * parameter value compliant to RFC 5792. It can be used in a fluent way.
 *
 *
 */
public interface PaAttributeValueRemediationParameterStringBuilder extends
        PaAttributeSubValueBuilder<PaAttributeValueRemediationParameterString> {

    /**
     * Set the remediation string.
     *
     * @param remediationString the remediation string
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaAttributeValueRemediationParameterStringBuilder setRemediationString(
            String remediationString) throws RuleException;

    /**
     * Sets the RFC 4646 language code.
     *
     * @param langCode the language code
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaAttributeValueRemediationParameterStringBuilder setLangCode(
            String langCode) throws RuleException;

}
