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

import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

/**
 * Generic builder to build a transport SASL mechanism selection message value
 * compliant to RFC 6876. It can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface PtTlsMessageValueSaslMechanismSelectionBuilder extends
        TransportMessageValueBuilder {

    /**
     * Sets the selected mechanism.
     *
     * @param mech the selected mechanism
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueSaslMechanismSelectionBuilder setMechanism(
            SaslMechanismEntry mech) throws RuleException;

    /**
     * Sets the optional initial SASL message.
     *
     * @param initialSaslMsg the initial SASL message
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueSaslMechanismSelectionBuilder
        setOptionalInitialSaslMessage(byte[] initialSaslMsg)
                throws RuleException;

}
