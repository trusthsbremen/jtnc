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

import java.util.Set;

import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.route.exception.NoRecipientFoundException;

/**
 * Generic message router to find interested IM(C/V)
 * for a message of a component type.
 *
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
