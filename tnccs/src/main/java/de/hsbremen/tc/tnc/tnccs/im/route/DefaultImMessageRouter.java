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
 * @author Carl-Heinz Genzel
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

}
