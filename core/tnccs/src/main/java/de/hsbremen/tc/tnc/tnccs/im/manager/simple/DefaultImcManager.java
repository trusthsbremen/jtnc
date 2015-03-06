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
package de.hsbremen.tc.tnc.tnccs.im.manager.simple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.im.manager.AbstractImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

/**
 * Default IMC manager, that manages IMC and its associated adapters.
 *
 *
 */
public class DefaultImcManager extends AbstractImManager<IMC> implements
        ImcManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImcManager.class);

    private final ImcAdapterFactory adapterFactory;
    private final TnccAdapterFactory tnccFactory;
    private final ImMessageRouter router;

    private Map<Long, ImcAdapter> adapterIndex;

    /**
     * Creates a default IMC manager with the given message router,
     * IMC adapter factory and TNCC adapter factory. The factories
     * are used to create the necessary adapters for every IMC. The
     * manager has a maximum IMC ID limit of (reserved IMC ID ANY - 1).
     *
     * @param router the message router
     * @param adapterFactory the IMC adapter factory
     * @param tnccFactory the TNCC adapter factory
     */
    public DefaultImcManager(final ImMessageRouter router,
            final ImcAdapterFactory adapterFactory,
            final TnccAdapterFactory tnccFactory) {
        this(router, adapterFactory, tnccFactory,
                (TNCConstants.TNC_IMCID_ANY - 1));
    }

    /**
     * Creates a default IMC manager with the given message router,
     * IMC adapter factory and TNCC adapter factory. The factories
     * are used to create the necessary adapters for every IMC. The
     * The maximum IMC ID limit is set by the maximum IMC ID parameter.
     *
     * @param router the message router
     * @param adapterFactory the IMC adapter factory
     * @param tnccFactory the TNCC adapter factory
     * @param maxImId the IMC ID limit
     */
    public DefaultImcManager(final ImMessageRouter router,
            final ImcAdapterFactory adapterFactory,
            final TnccAdapterFactory tnccFactory,
            final long maxImId) {

        super(router, maxImId);

        this.router = router;

        this.adapterFactory = adapterFactory;
        this.tnccFactory = tnccFactory;

        this.adapterIndex = new ConcurrentHashMap<>();

    }

    @Override
    protected void initialize(final long primaryId, final IMC im)
            throws TncException {
        TNCC tncc = this.tnccFactory.createTncc(im,
                new PrimaryImcIdAttribute(primaryId), this);

        try {
            im.initialize(tncc);
        } catch (TNCException e) {
            throw new TncException(e);
        }

        ImcAdapter adapter = this.adapterFactory
                .createImcAdapter(im, primaryId);
        this.adapterIndex.put(primaryId, adapter);

    }

    @Override
    protected void terminate(final long primaryId) {
        if (this.adapterIndex.containsKey(primaryId)) {
            ImcAdapter adapter = this.adapterIndex.remove(primaryId);
            try {
                adapter.terminate();
            } catch (TerminatedException e) {
                // ignore
            }
        }
    }

    @Override
    public Map<Long, ImcAdapter> getAdapter() {
        return new HashMap<>(this.adapterIndex);
    }

    @Override
    public ImMessageRouter getRouter() {
        return this.router;
    }

    @Override
    public void notifyFatalError(final long primaryId, final TncException e) {
        LOGGER.error("IMC with ID " + primaryId
                + "has thrown a fatal exception"
                + "and will be removed.", e);

        super.remove(primaryId);
    }

    @Override
    public void terminate() {
        Set<Long> keys = new HashSet<>(this.adapterIndex.keySet());
        for (Iterator<Long> iter = keys.iterator(); iter.hasNext();) {
            Long key = iter.next();
            super.remove(key);
        }
    }

    /**
     * Holds a primary ID of an IMC and makes it
     * accessible thru generic attribute getters and
     * setters.
     *
         *
     */
    private class PrimaryImcIdAttribute implements Attributed {

        private final long primaryImId;

        /**
         * Creates the attribute holding the primary ID
         * of an IMC with the given primary ID.
         *
         * @param primaryImId the primary ID
         */
        public PrimaryImcIdAttribute(final long primaryImId) {
            this.primaryImId = primaryImId;
        }

        @Override
        public void setAttribute(final TncAttributeType type,
                final Object value) throws TncException {
            throw new UnsupportedOperationException(
                    "The operation setAttribute(...) is not supported, "
                    + "because there are no attributes to set.");

        }

        @Override
        public Object getAttribute(final TncAttributeType type)
                throws TncException {
            if (type.id() == TncClientAttributeTypeEnum
                    .TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id()) {
                return new Long(this.primaryImId);
            }
            throw new TncException("The attribute with ID " + type.id()
                    + " is unknown.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }
}
