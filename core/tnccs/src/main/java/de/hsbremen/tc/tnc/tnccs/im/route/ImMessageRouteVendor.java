/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
