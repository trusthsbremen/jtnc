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
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.validate.rules.SaslMechanismName;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport SASL mechanism selection message value compliant
 * to RFC 6876. It evaluates the given values and can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageValueSaslMechanismSelectionBuilderIetf implements
        PtTlsMessageValueSaslMechanismSelectionBuilder {

    private static final byte LENGTH_FIELDS_AND_RESERVED_LENGTH = 1;

    private long length;
    private SaslMechanismEntry mechanism;
    private byte[] initialSaslMsg;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Mechanism: null</li>
     * <li>Message: null</li>
     * </ul>
     */
    public PtTlsMessageValueSaslMechanismSelectionBuilderIetf() {
        this.length = 0;
        this.mechanism = null;
        this.initialSaslMsg = null;
    }

    @Override
    public PtTlsMessageValueSaslMechanismSelectionBuilder setMechanism(
            final SaslMechanismEntry mech) throws RuleException {

        if (mech != null) {
            SaslMechanismName.check(mech.getName());
            this.mechanism = mech;
        }

        return this;
    }

    @Override
    public PtTlsMessageValueSaslMechanismSelectionBuilder
        setOptionalInitialSaslMessage(final byte[] initialSaslMsg)
                throws RuleException {

        if (initialSaslMsg != null) {
            this.initialSaslMsg = initialSaslMsg;
        }

        return this;
    }

    @Override
    public PtTlsMessageValueSaslMechanismSelection toObject() {

        if (this.mechanism == null) {
            throw new IllegalStateException(
                    "The SASL mechanism has to be set.");
        }

        this.length = (this.mechanism.getNameLength()
                + LENGTH_FIELDS_AND_RESERVED_LENGTH);

        if (this.initialSaslMsg != null) {
            this.length += this.initialSaslMsg.length;
            return new PtTlsMessageValueSaslMechanismSelection(this.length,
                    this.mechanism, this.initialSaslMsg);
        } else {
            return new PtTlsMessageValueSaslMechanismSelection(this.length,
                    this.mechanism);
        }
    }

    @Override
    public PtTlsMessageValueSaslMechanismSelectionBuilder newInstance() {
        return new PtTlsMessageValueSaslMechanismSelectionBuilderIetf();
    }

}
