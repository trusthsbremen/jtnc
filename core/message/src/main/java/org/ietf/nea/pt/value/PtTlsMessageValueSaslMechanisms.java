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
import java.util.Collections;
import java.util.List;

import org.ietf.nea.pt.value.util.SaslMechanismEntry;

/**
 * IETF RFC 6876 transport SASL mechanisms message value.
 *
 *
 */
public class PtTlsMessageValueSaslMechanisms extends AbstractPtTlsMessageValue {

    private final List<SaslMechanismEntry> mechanisms;

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param mechanisms the list of supported SASL mechanisms
     */
    PtTlsMessageValueSaslMechanisms(final long length,
            final List<SaslMechanismEntry> mechanisms) {
        super(length);
        this.mechanisms = (mechanisms != null) ? mechanisms
                : new ArrayList<SaslMechanismEntry>(0);
    }

    /**
     * Returns the list of supported SASL mechanisms.
     *
     * @return the lost of supported SASL mechanisms
     */
    public List<SaslMechanismEntry> getMechanisms() {
        return Collections.unmodifiableList(this.mechanisms);
    }

}
