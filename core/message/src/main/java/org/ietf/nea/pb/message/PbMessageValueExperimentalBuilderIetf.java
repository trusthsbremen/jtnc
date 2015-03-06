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

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS experimental message value compliant to RFC 5793.
 * It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbMessageValueExperimentalBuilderIetf implements
        PbMessageValueExperimentalBuilder {

    private long length;
    private String content; // variable string

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Content: ""</li>
     * </ul>
     */
    public PbMessageValueExperimentalBuilderIetf() {
        this.length = 0;
        this.content = "";
    }

    @Override
    public PbMessageValueExperimentalBuilder setMessage(final String content)
            throws RuleException {
        // no checks necessary because experimental
        if (content != null) {
            this.content = content;
            this.length = content.getBytes().length;
        }

        return this;
    }

    @Override
    public PbMessageValueExperimental toObject() {

        return new PbMessageValueExperimental(this.length, this.content);
    }

    @Override
    public PbMessageValueExperimentalBuilder newInstance() {

        return new PbMessageValueExperimentalBuilderIetf();
    }

}
