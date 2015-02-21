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

import java.util.Set;

import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.route.exception.NoRecipientFoundException;

/**
 * Generic message router to find interested IM(C/V)
 * for a message of a component type.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ImMessageRouter {

    /**
     * Updates the route table for the IM(C/V) with the given primary ID
     * using the given set of supported message types.
     *
     * @param primaryId the IM(C/V) ID
     * @param types the set of supported messages types
     */
    void updateMap(Long primaryId, Set<SupportedMessageType> types);

    /**
     * Removes the IM(C/V) with the given primary ID and its
     * message type entries from the table.
     *
     * @param primaryId the IM(C/V) ID
     */
    void remove(Long primaryId);

    /**
     * Adds an additional ID for an IM(C/V) with the given
     * primary ID to the route table for exclusive delivery.
     *
     * @param primaryId the IM(C/V) ID
     * @param additionalId the additional IM(C/V) ID
     */
    void addExclusiveId(Long primaryId, long additionalId);

    /**
     * Finds a set of IM(C/V) recipients for a message type described
     * by the given vendor ID and message type ID in the route table.
     * (multicast routing)
     *
     * @param vendorId the message type vendor ID
     * @param messageType the message type ID
     * @return a set of recipients
     * @throws NoRecipientFoundException if no recipient was found
     */
    Set<Long> findRecipientIds(long vendorId, long messageType)
            throws NoRecipientFoundException;

    /**
     * Finds an IM(C/V) recipient with the given primary/additional ID
     * for a message type described by the given vendor ID and message type ID
     * in the route table. (unicast routing)
     *
     * @param recipientId the recipients primary/additional ID
     * @param vendorId the message type vendor ID
     * @param messageType the message type ID
     * @return the recipients primary ID
     * @throws NoRecipientFoundException if no recipient was found
     */
    Long findExclRecipientId(Long recipientId, long vendorId, long messageType)
            throws NoRecipientFoundException;
}
