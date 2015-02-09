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
 * @author Carl-Heinz Genzel
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
