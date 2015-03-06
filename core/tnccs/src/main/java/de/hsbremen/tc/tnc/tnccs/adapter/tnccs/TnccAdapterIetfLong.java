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

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

/**
 * TNCC adapter according to IETF/TCG specifications.
 * Implementing an IF-IMC TNCC interface with long addressing
 * support.
 *
 *
 */
class TnccAdapterIetfLong extends TnccAdapterIetf implements TNCCLong {

    /**
     * Creates a TNCC adapter with the specified arguments.
     * @param moduleManager the IMC manager
     * @param attributes the accessible TNCC attributes
     * @param listener the global handshake retry listener
     */
    TnccAdapterIetfLong(final ImManager<IMC> moduleManager,
            final Attributed attributes,
            final GlobalHandshakeRetryListener listener) {
        super(moduleManager, attributes, listener);
    }

    @Override
    public void reportMessageTypesLong(final IMC imc,
            final long[] supportedVendorIDs,
            final long[] supportedSubtypes) throws TNCException {

        Set<SupportedMessageType> sTypes = new HashSet<>();

        if (supportedVendorIDs != null && supportedSubtypes != null) {

            if (supportedVendorIDs.length != supportedSubtypes.length) {
                throw new TNCException(new StringBuilder()
                    .append("The provided arrays have a different length. (")
                            .append(supportedVendorIDs.length).append(":")
                            .append(supportedSubtypes.length).append(")")
                            .toString(),
                        TNCException.TNC_RESULT_INVALID_PARAMETER);
            }

            for (int i = 0; i < supportedVendorIDs.length; i++) {
                try {
                    SupportedMessageType mType = SupportedMessageTypeFactory
                            .createSupportedMessageType(supportedVendorIDs[i],
                                    supportedSubtypes[i]);
                    sTypes.add(mType);

                } catch (IllegalArgumentException e) {
                    throw new TNCException(e.getMessage(),
                            TNCException.TNC_RESULT_INVALID_PARAMETER);
                }
            }
        }

        try {
            super.getManager().reportSupportedMessagesTypes(imc, sTypes);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    @Override
    public long reserveAdditionalIMCID(final IMC imc) throws TNCException {
        try {
            return super.getManager().reserveAdditionalId(imc);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

}
