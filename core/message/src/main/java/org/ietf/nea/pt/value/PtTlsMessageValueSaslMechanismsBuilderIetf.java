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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pt.validate.rules.SaslMechanismName;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a transport SASL mechanisms message value compliant to RFC
 * 6876. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PtTlsMessageValueSaslMechanismsBuilderIetf implements
        PtTlsMessageValueSaslMechanismsBuilder {

    private static final byte LENGTH_FIELDS_AND_RESERVED_LENGTH = 1;

    private long length;
    private List<SaslMechanismEntry> mechanisms;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Entries: Empty list</li>
     * </ul>
     */
    public PtTlsMessageValueSaslMechanismsBuilderIetf() {
        this.length = 0;
        this.mechanisms = new LinkedList<>();
    }

    @Override
    public PtTlsMessageValueSaslMechanismsBuilder addMechanism(
            final SaslMechanismEntry... mechs) throws RuleException {

        List<SaslMechanismEntry> temp = new ArrayList<>();

        if (mechs != null) {
            for (SaslMechanismEntry mech : mechs) {
                if (mech != null) {
                    SaslMechanismName.check(mech.getName());
                    temp.add(mech);
                }
            }
        }

        this.mechanisms.addAll(temp);
        this.updateLength();

        return this;
    }

    /**
     * Updates length according to the values set.
     */
    private void updateLength() {
        this.length = 0;
        for (SaslMechanismEntry mech : this.mechanisms) {
            this.length += (mech.getNameLength()
                    + LENGTH_FIELDS_AND_RESERVED_LENGTH);
        }
    }

    @Override
    public PtTlsMessageValueSaslMechanisms toObject() {

        return new PtTlsMessageValueSaslMechanisms(this.length,
                this.mechanisms);
    }

    @Override
    public PtTlsMessageValueSaslMechanismsBuilder newInstance() {
        return new PtTlsMessageValueSaslMechanismsBuilderIetf();
    }

}
