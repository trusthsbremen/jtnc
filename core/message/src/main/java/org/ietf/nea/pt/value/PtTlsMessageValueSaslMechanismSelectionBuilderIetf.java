/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
package org.ietf.nea.pt.value;

import org.ietf.nea.pt.validate.rules.SaslMechanismName;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport SASL mechanism selection message value compliant
 * to RFC 6876. It evaluates the given values and can be used in a fluent way.
 *
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
