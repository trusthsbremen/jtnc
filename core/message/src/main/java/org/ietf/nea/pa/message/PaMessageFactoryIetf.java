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
package org.ietf.nea.pa.message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Factory utility to create an IETF RFC 5792 compliant integrity measurement
 * component message.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PaMessageFactoryIetf {

    /**
     * Private constructor should never be invoked.
     */
    private PaMessageFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates a message with the given values.
     *
     * @param identifier the connection unique message identifier
     * @param attributes the contained integrity measurement attributes
     * @return an IETF RFC 5792 compliant integrity measurement component
     * message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaMessage createMessage(final long identifier,
            final List<? extends ImAttribute> attributes)
                    throws ValidationException {

        List<? extends ImAttribute> attrs = (attributes != null) ? attributes
                : new ArrayList<ImAttribute>(0);

        PaMessageHeaderBuilderIetf builder = new PaMessageHeaderBuilderIetf();

        List<PaAttribute> filteredAttributes = new LinkedList<>();
        try {
            builder.setIdentifier(identifier);

            long l = 0;
            for (ImAttribute attr : attrs) {
                if (attr instanceof PaAttribute) {
                    PaAttribute a = (PaAttribute) attr;
                    l += a.getHeader().getLength();
                    filteredAttributes.add(a);
                } else {
                    throw new IllegalArgumentException("Attribute type "
                            + attr.getClass().getCanonicalName()
                            + " not supported. Attribute must be of type "
                            + PaMessage.class.getCanonicalName() + ".");
                }

            }

            builder.setLength(l
                    + PaAttributeTlvFixedLengthEnum.MESSAGE.length());

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e,
                    ValidationException.OFFSET_NOT_SET);
        }

        PaMessage msg = new PaMessage(builder.toObject(), filteredAttributes);

        return msg;
    }

}
