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
package de.hsbremen.tc.tnc.tnccs.im.route;

import java.util.List;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Generic route element composite to compose a route
 * table with routing elements. Collects recipients
 * with their message type interests.
 *
 * @author Carl-Heinz Genzel
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
