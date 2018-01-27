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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.route.exception.NoRecipientFoundException;

/**
 * Default message router to find interested IM(C/V)
 * for a message of a component type.
 *
 *
 */
public class DefaultImMessageRouter implements ImMessageRouter {

    private final ImMessageRouteBase<Long> routeMap;
    private final Map<Long, Set<Long>> imIds;
    /* This is the concurrency lock for the routing */
    private final ReentrantReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * Creates the default message router.
     */
    public DefaultImMessageRouter() {
        this.routeMap = new ImMessageRouteBase<>();
        this.imIds = new HashMap<>();
        this.lock = new ReentrantReadWriteLock(Boolean.TRUE);
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
    }

    @Override
    public void updateMap(final Long primaryId,
            final Set<SupportedMessageType> types) {
        this.writeLock.lock();
        try {
            this.routeMap.unsubscribe(primaryId);
            for (SupportedMessageType type : types) {
                this.routeMap.subscribe(primaryId, type);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void remove(final Long primaryId) {
        this.writeLock.lock();
        try {
            this.routeMap.unsubscribe(primaryId);
            this.imIds.remove(primaryId);
        } finally {
            this.writeLock.unlock();
        }

    }

    @Override
    public void addExclusiveId(final Long primaryId, final long additionalId) {
        this.writeLock.lock();
        try {
            if (this.imIds.containsKey(primaryId)) {
                this.imIds.get(primaryId).add(additionalId);
            } else {
                Set<Long> additional = new HashSet<Long>();
                additional.add(additionalId);
                this.imIds.put(primaryId, additional);
            }
        } finally {
            this.writeLock.unlock();
        }

    }

    @Override
    public Set<Long> findRecipientIds(final long vendorId,
            final long messageType) throws NoRecipientFoundException {

        List<Long> result = null;
        this.readLock.lock();
        try {
            result = this.routeMap.findRecipients(vendorId, messageType);
        } finally {
            this.readLock.unlock();
        }
        
        if (result != null) {
            return new HashSet<Long>(result);
        } else {
            throw new NoRecipientFoundException(new StringBuilder()
                .append("No recipients found for message type with vendor ID ")
                .append(vendorId)
                .append(" and message type ID ")
                .append(messageType)
                .append(".").toString(),
                    new Long(vendorId), new Long(messageType));
        }
    }

    @Override
    public Long findExclRecipientId(final Long recipientId, final long vendorId,
            final long messageType) throws NoRecipientFoundException {

        Long result = null;
        this.readLock.lock();
        try {
            List<Long> recipients = this.routeMap.findRecipients(vendorId,
                    messageType);
            if (recipients.contains(recipientId)) {
                result = (Long) recipientId;
            } else {
                for (Long long1 : recipients) {
                    if (this.imIds.containsKey(long1)) {
                        if (this.imIds.get(long1).contains(recipientId)) {
                            result = long1;
                        }
                    }
                }
            }
        } finally {
            this.readLock.unlock();
        }
        if (result != null) {
            return result;
        } else {
            throw new NoRecipientFoundException(new StringBuilder()
                .append("No recipient with ID ")
                .append(recipientId.toString())
                .append(" found for message type with vendor ID ")
                .append(vendorId)
                .append(" and message type ID ")
                .append(messageType)
                .append(".").toString(),
                    new Long(vendorId), new Long(messageType), recipientId);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultImMessageRouter [routeMap=" + this.routeMap.toString() + ", imIds="
                + this.imIds.toString() + "]";
    }

    
}
