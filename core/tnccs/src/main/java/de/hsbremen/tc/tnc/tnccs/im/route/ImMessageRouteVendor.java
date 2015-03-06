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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Vendor route element of a route table. Contains message
 * type route elements as sub elements.
 *
 *
 * @param <T> the recipient type
 */
class ImMessageRouteVendor<T> implements ImMessageRouteComponent<T> {

    private final Map<Long, ImMessageRouteComponent<T>> typeDispatcher;

    /**
     * Creates a vendor route element with a message type sub element for
     * the reserved message type ID ANY.
     *
     */
    ImMessageRouteVendor() {
        this.typeDispatcher = new LinkedHashMap<>();
        // create a always a slot for the ANY subscribers
        this.typeDispatcher.put(TNCConstants.TNC_SUBTYPE_ANY,
                new ImMessageRouteType<T>());
    }

    /**
     * Adds a new message type element responsible for
     * the given message type ID as sub element to the vendor element.
     *
     * @param id the message type ID
     * @param typeDispatcher the message type element
     */
    private void add(final Long id,
            final ImMessageRouteComponent<T> typeDispatcher) {
        this.typeDispatcher.put(id, typeDispatcher);
    }

    @Override
    public List<T> findRecipients(final Long vendorId, final Long messageType) {

        List<T> t = new ArrayList<>();

        if (this.typeDispatcher.containsKey(messageType)) {
            t.addAll(this.typeDispatcher.get(messageType).findRecipients(
                    vendorId, messageType));
        }
        // Dispatch always to the ANY subscribers.
        t.addAll(this.typeDispatcher.get(TNCConstants.TNC_SUBTYPE_ANY)
                .findRecipients(vendorId, messageType));

        return t;
    }

    @Override
    public void subscribe(final T connection, final SupportedMessageType type) {
        if (connection != null) {
            if (!this.typeDispatcher.containsKey(type.getType())) {
                this.add(type.getType(), new ImMessageRouteType<T>());
            }
            this.typeDispatcher.get(type.getType()).subscribe(connection, type);
        }
    }

    @Override
    public void unsubscribe(final T connection) {
        if (connection != null) {
            for (Iterator<Long> iter =
                    this.typeDispatcher.keySet().iterator(); iter
                    .hasNext();) {
                Long type = iter.next();
                this.typeDispatcher.get(type).unsubscribe(connection);
                if (this.typeDispatcher.get(type).countChildren() <= 0
                        && type != TNCConstants.TNC_SUBTYPE_ANY) {
                    iter.remove();
                }
            }
        }
    }

    @Override
    public long countChildren() {
        return this.typeDispatcher.size();
    }
}
