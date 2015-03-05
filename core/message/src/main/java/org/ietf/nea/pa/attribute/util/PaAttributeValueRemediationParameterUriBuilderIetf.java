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

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;
import org.ietf.nea.pa.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement URI remediation parameter value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaAttributeValueRemediationParameterUriBuilderIetf implements
        PaAttributeValueRemediationParameterUriBuilder {

    private long length;
    private URI uri;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>URI: null</li>
     * </ul>
     */
    public PaAttributeValueRemediationParameterUriBuilderIetf() {
        this.length = 0;
        this.uri = null;
    }

    @Override
    public PaAttributeValueRemediationParameterUriBuilder setUri(
            final String uri) throws RuleException {

        NoZeroString.check(uri);
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuleException("URI syntax not valid.", e, true,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.URI_SYNTAX_NOT_VALID.id(), uri);
        }
        this.length = this.uri.toString().getBytes().length;
        return this;
    }

    @Override
    public PaAttributeValueRemediationParameterUri toObject()
            throws RuleException {

        if (uri == null) {
            throw new IllegalStateException("A message value has to be set.");
        }

        return new PaAttributeValueRemediationParameterUri(this.length,
                this.uri);
    }

    @Override
    public PaAttributeValueRemediationParameterUriBuilder newInstance() {

        return new PaAttributeValueRemediationParameterUriBuilderIetf();
    }

}
