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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Root route element of a route table. Contains vendor
 * route elements as sub elements.
 *
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
                
                if (this.vendorDispatcher.get(vendorId).totalRecipients() <= 0
                        && vendorId != TNCConstants.TNC_VENDORID_ANY) {
                    iter.remove();
                }
            }
        }
    }

    @Override
    public long totalRecipients() {
        long count = 0;
        for(Entry<Long,ImMessageRouteComponent<T>> entry :
            this.vendorDispatcher.entrySet()) {
            
            if(Long.MAX_VALUE - entry.getValue().totalRecipients() <= count) {
                count += entry.getValue().totalRecipients();
            } else {
                return Long.MAX_VALUE;
            }
            
        }
        return count;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImMessageRouteBase " + this.vendorDispatcher.toString();
    }
}
