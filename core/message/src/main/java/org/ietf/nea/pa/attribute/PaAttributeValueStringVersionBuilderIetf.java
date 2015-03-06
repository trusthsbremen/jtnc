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

import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pa.validate.rules.StringLengthLimit;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement string version attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueStringVersionBuilderIetf implements
        PaAttributeValueStringVersionBuilder {

    private static final int MAX_STRING_LENGTH = 0xFF;

    private long length;
    private String productVersion;
    private String buildVersion;
    private String configVersion;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: Fixed value length only</li>
     * <li>Product version: ""</li>
     * <li>Build version: ""</li>
     * <li>Configuration version: ""</li>
     * </ul>
     */
    public PaAttributeValueStringVersionBuilderIetf() {
        this.length = PaAttributeTlvFixedLengthEnum.STR_VER.length();
        this.productVersion = "";
        this.buildVersion = "";
        this.configVersion = "";
    }

    @Override
    public PaAttributeValueStringVersionBuilder setProductVersion(
            final String productVersion) throws RuleException {
        if (productVersion != null) {
            NoNullTerminatedString.check(productVersion);
            StringLengthLimit.check(productVersion, MAX_STRING_LENGTH);
            this.productVersion = productVersion;
            this.updateLength();
        }
        return this;
    }

    @Override
    public PaAttributeValueStringVersionBuilder setBuildNumber(
            final String buildNumber) throws RuleException {
        if (buildNumber != null) {
            NoNullTerminatedString.check(buildNumber);
            StringLengthLimit.check(buildNumber, MAX_STRING_LENGTH);
            this.buildVersion = buildNumber;
            this.updateLength();
        }
        return this;
    }

    @Override
    public PaAttributeValueStringVersionBuilder setConfigurationVersion(
            final String configVersion) throws RuleException {
        if (configVersion != null) {
            NoNullTerminatedString.check(configVersion);
            StringLengthLimit.check(configVersion, MAX_STRING_LENGTH);
            this.configVersion = configVersion;
            this.updateLength();
        }
        return this;
    }

    @Override
    public PaAttributeValueStringVersion toObject() {
        return new PaAttributeValueStringVersion(this.length,
                this.productVersion, this.buildVersion, this.configVersion);
    }

    @Override
    public PaAttributeValueStringVersionBuilder newInstance() {
        return new PaAttributeValueStringVersionBuilderIetf();
    }

    /**
     * Updates the length according to the given version information.
     */
    private void updateLength() {
        this.length = PaAttributeTlvFixedLengthEnum.STR_VER.length();
        if (productVersion.length() > 0) {
            this.length += productVersion.getBytes(
                    Charset.forName("UTF-8")).length;
        }

        if (buildVersion.length() > 0) {
            this.length += buildVersion.getBytes(
                    Charset.forName("UTF-8")).length;
        }

        if (configVersion.length() > 0) {
            this.length += configVersion.getBytes(
                    Charset.forName("UTF-8")).length;
        }
    }

}
