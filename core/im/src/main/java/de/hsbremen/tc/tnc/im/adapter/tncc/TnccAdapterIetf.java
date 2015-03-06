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
package de.hsbremen.tc.tnc.im.adapter.tncc;

import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * TNCC adapter according to the IETF/TCG specifications.
 *
 */
class TnccAdapterIetf implements TnccAdapter, GlobalHandshakeRetryListener {

    private final IMC imc;
    private final TNCC tncc;

    /**
     * Creates a TNCC adapter based on the given TNCC for the given IMC.
     * @param imc the IMC
     * @param tncc the TNCC
     */
    TnccAdapterIetf(final IMC imc, final TNCC tncc) {
        this.imc = imc;
        this.tncc = tncc;
    }

    @Override
    public void reportMessageTypes(
            final Set<SupportedMessageType> supportedTypes)
            throws TncException {
        try {
            if (this.tncc instanceof TNCCLong) {

                long[][] messageTypes = SupportedMessageTypeFactory
                        .createSupportedMessageTypeArray(supportedTypes);
                ((TNCCLong) this.tncc).reportMessageTypesLong(this.imc,
                        messageTypes[0], messageTypes[1]);

            } else {

                long[] messageTypes = SupportedMessageTypeFactory
                        .createSupportedMessageTypeArrayLegacy(supportedTypes);
                this.tncc.reportMessageTypes(this.imc, messageTypes);
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
                this.tncc.requestHandshakeRetry(this.imc, reason.id());
            } catch (TNCException e) {
                throw new TncException(e);
            }

        } else {
            throw new TncException("Reason is not useable with IMC and TNCC.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    @Override
    public long reserveAdditionalId() throws TncException {
        if (this.tncc instanceof TNCCLong) {

            try {
                return ((TNCCLong) this.tncc).reserveAdditionalIMCID(this.imc);
            } catch (TNCException e) {
                throw new TncException(e);
            }

        }

        throw new UnsupportedOperationException(this.tncc.getClass().getName()
                + " instance is not of type " + TNCCLong.class.getSimpleName()
                + ".");
    }

    @Override
    public GlobalHandshakeRetryListener getHandshakeRetryListener() {
        return this;
    }

}
