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
package de.hsbremen.tc.tnc.tnccs.im.manager.simple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TncsAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.im.manager.AbstractImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImvManager;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

/**
 * Default IMV manager, that manages IMV and its associated adapters.
 *
 *
 */
public class DefaultImvManager extends AbstractImManager<IMV> implements
        ImvManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImvManager.class);

    private final ImvAdapterFactory adapterFactory;
    private final TncsAdapterFactory tncsFactory;
    private final ImMessageRouter router;

    private Map<Long, ImvAdapter> adapterIndex;

    /**
     * Creates a default IMV manager with the given message router,
     * IMV adapter factory and TNCS adapter factory. The factories
     * are used to create the necessary adapters for every IMV. The
     * manager has a maximum IMV ID limit of (reserved IMV ID ANY - 1).
     *
     * @param router the message router
     * @param adapterFactory the IMV adapter factory
     * @param tncsFactory the TNCS adapter factory
     */
    public DefaultImvManager(final ImMessageRouter router,
            final ImvAdapterFactory adapterFactory,
            final TncsAdapterFactory tncsFactory) {
        this(router, adapterFactory, tncsFactory,
                (TNCConstants.TNC_IMCID_ANY - 1));
    }

    /**
     * Creates a default IMV manager with the given message router,
     * IMV adapter factory and TNCS adapter factory. The factories
     * are used to create the necessary adapters for every IMV. The
     * The maximum IMV ID limit is set by the maximum IMV ID parameter.
     *
     * @param router the message router
     * @param adapterFactory the IMV adapter factory
     * @param tncsFactory the TNCS adapter factory
     * @param maxImId the IMV ID limit
     */
    public DefaultImvManager(final ImMessageRouter router,
            final ImvAdapterFactory adapterFactory,
            final TncsAdapterFactory tncsFactory,
            final long maxImId) {

        super(router, maxImId);

        this.router = router;

        this.adapterFactory = adapterFactory;
        this.tncsFactory = tncsFactory;

        this.adapterIndex = new ConcurrentHashMap<>();

    }

    @Override
    protected void initialize(final long primaryId, final IMV im)
            throws TncException {
        TNCS tncc = this.tncsFactory.createTncs(im, new PrimaryImvIdAttribute(
                primaryId), this);

        try {
            im.initialize(tncc);
        } catch (TNCException e) {
            throw new TncException(e);
        }

        ImvAdapter adapter = this.adapterFactory
                .createImvAdapter(im, primaryId);
        this.adapterIndex.put(primaryId, adapter);

    }

    @Override
    protected void terminate(final long primaryId) {
        if (this.adapterIndex.containsKey(primaryId)) {
            ImvAdapter adapter = this.adapterIndex.remove(primaryId);
            try {
                adapter.terminate();
            } catch (TerminatedException e) {
                // ignore
            }
        }
    }

    @Override
    public Map<Long, ImvAdapter> getAdapter() {
        return new HashMap<>(this.adapterIndex);
    }

    @Override
    public ImMessageRouter getRouter() {
        return this.router;
    }

    @Override
    public void notifyFatalError(final long primaryId, final TncException e) {
        LOGGER.error("IMV with ID " + primaryId
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
     * Holds a primary ID of an IMV and makes it accessible thru generic
     * attribute getters and setters.
     *
         *
     */
    private class PrimaryImvIdAttribute implements Attributed {

        private final long primaryImId;

        /**
         * Creates the attribute holding the primary ID of an IMV with the given
         * primary ID.
         *
         * @param primaryImId the primary ID
         */
        public PrimaryImvIdAttribute(final long primaryImId) {
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
            if (type.id() == TncServerAttributeTypeEnum
                    .TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()) {
                return new Long(this.primaryImId);
            }
            throw new TncException("The attribute with ID " + type.id()
                    + " is unknown.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }
}
