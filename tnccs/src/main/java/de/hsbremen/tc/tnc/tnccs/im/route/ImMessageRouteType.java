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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Message type route element of a route table. Contains recipients
 * interested in the message type.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the recipient type
 */
class ImMessageRouteType<T> implements ImMessageRouteComponent<T> {

    // Work around for the fact, that a set has no get(Object o) method.
    private final Map<T, T> subscribers;

    /**
     * Creates a message type route element.
     *
     */
    ImMessageRouteType() {
        this.subscribers = new LinkedHashMap<>();
    }

    @Override
    public List<T> findRecipients(final Long vendorId, final Long messageType) {
        return new LinkedList<>(subscribers.values());
    }

    @Override
    public void subscribe(final T connection, final SupportedMessageType type) {
        if (connection != null) {
            this.subscribers.put(connection, connection);
        }

    }

    @Override
    public void unsubscribe(final T connection) {
        if (connection != null && this.subscribers.containsKey(connection)) {
            this.subscribers.remove(connection);
        }
    }

    @Override
    public long countChildren() {
        return this.subscribers.size();
    }

}
