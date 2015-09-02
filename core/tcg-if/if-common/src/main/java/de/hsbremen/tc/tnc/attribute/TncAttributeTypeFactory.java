/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package de.hsbremen.tc.tnc.attribute;

import java.util.List;

/**
 * Generic attribute type factory.
 *
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
