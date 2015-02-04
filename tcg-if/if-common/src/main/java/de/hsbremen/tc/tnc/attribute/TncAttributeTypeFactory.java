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
package de.hsbremen.tc.tnc.attribute;

import java.util.List;

/**
 * Generic attribute type factory.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface TncAttributeTypeFactory {

    /**
     * Creates an attribute type from a given type ID.
     * @param id the type ID
     * @return the attribute type with the given ID or null
     */
    TncAttributeType fromId(long id);

    /**
     * Creates an attribute type from a given type ID,
     * if it is a type used by a client.
     *
     * @param id the type ID
     * @return the attribute type with the given ID or null
     */
    TncAttributeType fromIdClientOnly(long id);

    /**
     * Creates an attribute type from a given type ID,
     * if it is a type used by a server.
     *
     * @param id the type ID
     * @return the attribute type with the given ID or null
     */
    TncAttributeType fromIdServerOnly(long id);

    /**
     * Creates a list with all known attribute types.
     *
     * @return the list of attribute types, list may be empty
     */
    List<TncAttributeType> getAllTypes();

    /**
     * Creates a list with all known client attribute types.
     *
     * @return the list of attribute types, list may be empty
     */
    List<TncAttributeType> getClientTypes();

    /**
     * Creates a list with all known server attribute types.
     *
     * @return the list of attribute types, list may be empty
     */
    List<TncAttributeType> getServerTypes();
}
