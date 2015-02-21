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
 * Root route element of a route table. Contains vendor
 * route elements as sub elements.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the recipient type
 */
public class ImMessageRouteBase<T> implements ImMessageRouteComponent<T> {

    private final Map<Long, ImMessageRouteComponent<T>> vendorDispatcher;

    /**
     * Creates a root route element with a vendor sub element for
     * the reserved vendor ID ANY.
     *
     */
    public ImMessageRouteBase() {
        this.vendorDispatcher = new LinkedHashMap<>();
        // create a always a slot for the ANY subscribers
        this.add(TNCConstants.TNC_VENDORID_ANY, new ImMessageRouteVendor<T>());
    }

    /**
     * Adds a new vendor element responsible for
     * the given vendor ID as sub element to the root element.
     *
     * @param id the vendor ID
     * @param vendorDispatcher the vendor element
     */
    private void add(final Long id,
            final ImMessageRouteVendor<T> vendorDispatcher) {
        this.vendorDispatcher.put(id, vendorDispatcher);
    }

    @Override
    public List<T> findRecipients(final Long vendorId, final Long messageType) {

        List<T> t = new ArrayList<>();

        if (this.vendorDispatcher.containsKey(vendorId)) {
            t.addAll(this.vendorDispatcher.get(vendorId).findRecipients(
                    vendorId, messageType));
        }
        // Dispatch always to the ANY subscribers.
        t.addAll(this.vendorDispatcher.get(TNCConstants.TNC_VENDORID_ANY)
                .findRecipients(vendorId, messageType));

        return t;
    }

    @Override
    public void subscribe(final T connection, final SupportedMessageType type) {
        if (connection != null) {
            if (!this.vendorDispatcher.containsKey(type.getVendorId())) {
                this.add(type.getVendorId(), new ImMessageRouteVendor<T>());
            }
            this.vendorDispatcher.get(type.getVendorId()).subscribe(connection,
                    type);
        }
    }

    @Override
    public void unsubscribe(final T connection) {
        if (connection != null) {
            for (Iterator<Long> iter = this.vendorDispatcher.keySet()
                    .iterator(); iter.hasNext();) {
                Long vendorId = iter.next();
                this.vendorDispatcher.get(vendorId).unsubscribe(connection);
                if (this.vendorDispatcher.get(vendorId).countChildren() <= 0
                        && vendorId != TNCConstants.TNC_VENDORID_ANY) {
                    iter.remove();
                }
            }
        }
    }

    @Override
    public long countChildren() {
        return this.vendorDispatcher.size();
    }
}
