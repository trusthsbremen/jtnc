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
package org.ietf.nea.pt.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;

/**
 * Default helper object to pass on a transport message and minor
 * validation errors after parsing for further processing.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultTransportMessageContainer implements
        TransportMessageContainer {

    private final TransportMessage message;
    private final List<ValidationException> exceptions;

    /**
     * Creates the helper with the given message and minor errors.
     *
     * @param message the integrity measurement component message
     * @param exceptions the minor validation errors
     */
    public DefaultTransportMessageContainer(final TransportMessage message,
            final List<ValidationException> exceptions) {

        this.message = message;
        this.exceptions = ((exceptions != null) ? exceptions
                : new ArrayList<ValidationException>(0));
    }

    @Override
    public List<ValidationException> getExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    @Override
    public TransportMessage getResult() {
        return this.message;
    }

}
