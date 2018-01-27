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
package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

/**
 * Generic base for an IM(C/V) manager. Especially
 * important for inheritance.
 *
 *
 * @param <T> the managed type (e.g. IMC or IMV)
 */
public abstract class AbstractImManager<T> implements ImManager<T> {

    private long idDispensor;
    private Deque<Long> idRecyclingBin;
    private final long maxImId;

    private Map<Long, T> imIndex;
    private Map<T, Long> ims;

    private ImMessageRouter router;

    /**
     * Creates a IM(C/V) manager base with the given message router and
     * a maximum IM(C/V) ID limiting the number of available IDs.
     *
     * @param router the message router
     * @param maxImId the maximum IM(C/V) ID
     */
    public AbstractImManager(final ImMessageRouter router, final long maxImId) {

        this.idDispensor = 0;
        /* Use this because sessions and IM management may have threads */
        this.idRecyclingBin = new ConcurrentLinkedDeque<>();
        /* Use this because sessions and IM management may have threads */
        this.ims = new ConcurrentHashMap<>();
        /* Use this because sessions and IM management may have threads */
        this.imIndex = new ConcurrentHashMap<>();
        this.router = router;
        this.maxImId = maxImId;
    }

    @Override
    public long add(final T im) throws ImInitializeException {
        long primaryId;
        try {
            primaryId = this.reserveId();
        } catch (TncException e) {
            throw new ImInitializeException(
                    "Intialization of IMC failed. IMC will be removed.", e);
        }

        this.imIndex.put(primaryId, im);
        this.ims.put(im, primaryId);

        try {
            this.initialize(primaryId, im);
        } catch (TncException e) {
            this.imIndex.remove(primaryId);
            this.ims.remove(im);
            this.idRecyclingBin.add(primaryId);
            throw new ImInitializeException(
                    "Intialization of IMC failed. IMC will be removed.", e);
        }

        return primaryId;
    }

    @Override
    public void remove(final long primaryId) {

        this.terminate(primaryId);
        T imc = this.imIndex.remove(primaryId);
        this.router.remove(primaryId);
        if (imc != null) {
            this.ims.remove(imc);
        }

        this.idRecyclingBin.add(primaryId);

    }

    @Override
    public Map<Long, T> getManaged() {
        return new HashMap<Long, T>(this.imIndex);
    }

    @Override
    public long reserveAdditionalId(final T im) throws TncException {
        if (this.ims.containsKey(im)) {
            long additionalId = this.reserveId();
            this.router.addExclusiveId(this.ims.get(im), additionalId);
            return additionalId;
        }

        throw new TncException("The given IMC/V "
                + im.getClass().getCanonicalName() + " is unknown.",
                TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
    }

    @Override
    public void reportSupportedMessagesTypes(final T im,
            final Set<SupportedMessageType> types) throws TncException {

        if (this.ims.containsKey(im)) {
            this.router.updateMap(this.ims.get(im), types);
        } else {
            throw new TncException("The given IMC/V "
                    + im.getClass().getCanonicalName() + " is unknown.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    /**
     * Initializes the given IM(C/V) using the given primary ID.
     * Must be implemented by an extending class and is called
     * at the end of the add() method.
     *
     * @param primaryId the primary ID
     * @param im the IM(C/V)
     * @throws TncException if initialization fails
     */
    protected abstract void initialize(final long primaryId, final T im)
            throws TncException;

    /**
     * Terminates the IM(C/V) with the given primary ID. Must be
     * implemented by an extending class and is called, at the beginning
     * of the remove() method.
     *
     * @param id the IM(C/V) ID
     */
    protected abstract void terminate(final long id);

    /**
     * Reserves an ID from the ID pool.
     * It has an ID reuse mechanism. IDs from removed IM(C/V)
     * will be cached and newly assigned before a new ID is
     * generated.
     *
     * @return an ID
     * @throws TncException if ID reservation fails because no IDs are left
     */
    private long reserveId() throws TncException {
        if (!this.idRecyclingBin.isEmpty()) {
            return this.idRecyclingBin.pop();
        } else {
            if (idDispensor < this.maxImId) {
                return ++idDispensor;
            } else {
                throw new TncException("No Ids left.",
                        TncExceptionCodeEnum.TNC_RESULT_OTHER);
            }
        }
    }
}
