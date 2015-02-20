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
package de.hsbremen.tc.tnc.tnccs.im.manager.simple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultImcManager extends AbstractImManager<IMC> implements
        ImcManager {

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
    public Map<Long, ImcAdapter> getAdapter() {
        return new HashMap<>(this.adapterIndex);
    }

    @Override
    public ImMessageRouter getRouter() {
        return this.router;
    }

    @Override
    public void removeAdapter(final long id) {
        if (this.adapterIndex.containsKey(id)) {
            ImcAdapter adapter = this.adapterIndex.remove(id);
            try {
                adapter.terminate();
            } catch (TerminatedException e) {
                // ignore
            }

        }
        super.remove(id);
    }

    @Override
    public void terminate() {
        Set<Long> keys = new HashSet<>(this.adapterIndex.keySet());
        for (Iterator<Long> iter = keys.iterator(); iter.hasNext();) {
            Long key = iter.next();
            this.removeAdapter(key);
        }
    }

    /**
     * Holds a primary ID of an IMC and makes it
     * accessible thru generic attribute getters and
     * setters.
     *
     * @author Carl-Heinz Genzel
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
