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
package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Holds a message concerning an integrity measurement
 * component including the message header values and
 * the integrity measurement attributes as parsed objects. It can also hold
 * minor ValidationException objects, which should be handled later and a
 * raw message header for the creation of a error message.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class ImFaultyObjectComponent extends ImObjectComponent {

    private final List<ValidationException> exceptions;
    private final byte[] messageHeader;

    /**
     * Creates a component with the necessary address attributes and
     * the integrity measurement attributes as parsed objects as well as
     * the necessary objects for error handling.
     *
     * @param flags the message flags
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param attributes the measurement attributes
     * @param exceptions the list of minor exception
     * @param messageHeader the raw message header
     */
    ImFaultyObjectComponent(final PaComponentFlagsEnum[] flags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
            final List<? extends ImAttribute> attributes,
            final List<ValidationException> exceptions,
            final byte[] messageHeader) {
        super(flags, vendorId, type, collectorId, validatorId, attributes);
        this.exceptions = exceptions;
        this.messageHeader = messageHeader;
    }

    /**
     * Returns the unmodifiable list of minor ValidationException objects.
     * @return the list of minor exceptions
     */
    public List<ValidationException> getExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    /**
     * Returns a copy of the raw message header.
     * @return the raw message header
     */
    public byte[] getMessageHeader() {
        return Arrays.copyOf(this.messageHeader, this.messageHeader.length);
    }

}
