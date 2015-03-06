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
package de.hsbremen.tc.tnc.im.adapter.tncs;

import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;
import org.trustedcomputinggroup.tnc.ifimv.TNCSLong;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * TNCS adapter according to the IETF/TCG specifications.
 *
 *
 */
class TncsAdapterIetf implements TncsAdapter, GlobalHandshakeRetryListener {

    private final IMV imv;
    private final TNCS tncs;

    /**
     * Creates a TNCS adapter based on the given TNCS for the given IMV.
     *
     * @param imv the IMV
     * @param tncs the TNCS
     */
    TncsAdapterIetf(final IMV imv, final TNCS tncs) {
        this.imv = imv;
        this.tncs = tncs;
    }

    @Override
    public void reportMessageTypes(
            final Set<SupportedMessageType> supportedTypes)
            throws TncException {
        try {
            if (this.tncs instanceof TNCSLong) {

                long[][] messageTypes = SupportedMessageTypeFactory
                        .createSupportedMessageTypeArray(supportedTypes);
                ((TNCSLong) this.tncs).reportMessageTypesLong(this.imv,
                        messageTypes[0], messageTypes[1]);

            } else {

                long[] messageTypes = SupportedMessageTypeFactory
                        .createSupportedMessageTypeArrayLegacy(supportedTypes);
                this.tncs.reportMessageTypes(this.imv, messageTypes);
            }
        } catch (TNCException e) {
            throw new TncException(e);
        }
    }

    @Override
    public void requestGlobalHandshakeRetry(
            final ImHandshakeRetryReasonEnum reason)
            throws TncException {
        if (reason.toString().contains("IMC")) {

            try {
                this.tncs.requestHandshakeRetry(this.imv, reason.id());
            } catch (TNCException e) {
                throw new TncException(e);
            }

        } else {
            throw new TncException("Reason is not useable with IMC and TNCS.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    @Override
    public long reserveAdditionalId() throws TncException {
        if (this.tncs instanceof TNCSLong) {

            try {
                return ((TNCSLong) this.tncs).reserveAdditionalIMVID(this.imv);
            } catch (TNCException e) {
                throw new TncException(e);
            }

        }

        throw new UnsupportedOperationException(this.tncs.getClass().getName()
                + " instance is not of type " + TNCSLong.class.getSimpleName()
                + ".");
    }

    @Override
    public GlobalHandshakeRetryListener getHandshakeRetryListener() {
        return this;
    }

}
