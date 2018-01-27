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
                    System.out.println(l);
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
