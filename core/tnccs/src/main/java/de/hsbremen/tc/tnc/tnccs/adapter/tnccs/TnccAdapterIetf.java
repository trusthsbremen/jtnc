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
package de.hsbremen.tc.tnc.tnccs.adapter.tnccs;

import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

/**
 * TNCC adapter according to IETF/TCG specifications.
 * Implementing a simple IF-IMC TNCC interface.
 *
 *
 */
class TnccAdapterIetf implements TNCC, AttributeSupport {

    private final ImManager<IMC> moduleManager;
    private final GlobalHandshakeRetryListener listener;
    private final Attributed attributes;

    /**
     * Creates a TNCC adapter with the specified arguments.
     * @param moduleManager the IMC manager
     * @param attributes the accessible TNCC attributes
     * @param listener the global handshake retry listener
     */
    TnccAdapterIetf(final ImManager<IMC> moduleManager,
            final Attributed attributes,
            final GlobalHandshakeRetryListener listener) {
        this.moduleManager = moduleManager;
        this.attributes = attributes;
        this.listener = listener;
    }

    /**
     * Returns the IMC manager. Especially important for inheritance.
     *
     * @return the IMC manager
     */
    protected ImManager<IMC> getManager() {
        return this.moduleManager;
    }

    @Override
    public void reportMessageTypes(final IMC imc, final long[] supportedTypes)
            throws TNCException {

        Set<SupportedMessageType> sTypes = new HashSet<>();

        if (supportedTypes != null) {
            for (long l : supportedTypes) {
                try {
                    SupportedMessageType mType = SupportedMessageTypeFactory
                            .createSupportedMessageTypeLegacy(l);
                    sTypes.add(mType);
                } catch (IllegalArgumentException e) {
                    throw new TNCException(e.getMessage(),
                            TNCException.TNC_RESULT_INVALID_PARAMETER);
                }
            }
        }

        try {
            this.moduleManager.reportSupportedMessagesTypes(imc, sTypes);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    @Override
    public void requestHandshakeRetry(final IMC imc, final long reason)
            throws TNCException {
        // TODO is the IMC needed as parameter?
        try {
            this.listener
                    .requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum
                            .fromId(reason));
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    @Override
    public Object getAttribute(final long attributeID) throws TNCException {
        try {
            return this.attributes.getAttribute(DefaultTncAttributeTypeFactory
                    .getInstance().fromId(attributeID));
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

    @Override
    public void setAttribute(final long attributeID,
            final Object attributeValue) throws TNCException {
        try {
            this.attributes.setAttribute(DefaultTncAttributeTypeFactory
                    .getInstance().fromId(attributeID), attributeValue);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

}
