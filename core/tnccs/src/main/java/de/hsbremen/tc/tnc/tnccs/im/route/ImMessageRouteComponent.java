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
package de.hsbremen.tc.tnc.tnccs.im.route;

import java.util.List;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Generic route element composite to compose a route
 * table with routing elements. Collects recipients
 * with their message type interests.
 *
 *
 * @param <T> the recipient type
 */
interface ImMessageRouteComponent<T> {

    /**
     * Finds a set of recipients for a message type described
     * by the given vendor ID and message type ID.
     *
     * @param vendorId the message type vendor ID
     * @param messageType the message type ID
     * @return a set of recipients
     */
    List<T> findRecipients(Long vendorId, Long messageType);

    /**
     * Adds a recipient to the route table using the
     * given set of supported message types. Subscribe does
     * not properly update message types of existing recipient.
     * Use remove and a new subscribe to update a set of
     * supported message types for an existing recipient.
     *
     * @param recipient the recipient
     * @param types the set of supported messages types
     */
    void subscribe(T recipient, SupportedMessageType types);

    /**
     * Removes a recipient from the route table using the
     * given set of supported message types.
     *
     * @param recipient the recipient
     */
    void unsubscribe(T recipient);

    /**
     * Returns the children count of this route element.
     * @return the children count
     */
    long countChildren();

}
