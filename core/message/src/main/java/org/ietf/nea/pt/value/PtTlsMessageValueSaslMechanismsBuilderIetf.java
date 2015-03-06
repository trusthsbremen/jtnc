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
