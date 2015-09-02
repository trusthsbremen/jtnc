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

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCSLong;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

/**
 * TNCS adapter according to IETF/TCG specifications.
 * Implementing an IF-IMV TNCS interface with long addressing
 * support.
 *
 *
 */
class TncsAdapterIetfLong extends TncsAdapterIetf implements TNCSLong {

    /**
     * Creates a TNCS adapter with the specified arguments.
     *
     * @param moduleManager the IMV manager
     * @param attributes the accessible TNCC attributes
     * @param listener the global handshake retry listener
     */
    TncsAdapterIetfLong(final ImManager<IMV> moduleManager,
            final Attributed attributes,
            final GlobalHandshakeRetryListener listener) {
        super(moduleManager, attributes, listener);
    }

    @Override
    public void reportMessageTypesLong(final IMV imv,
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
            super.getManager().reportSupportedMessagesTypes(imv, sTypes);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

    @Override
    public long reserveAdditionalIMVID(final IMV imv) throws TNCException {
        try {
            return super.getManager().reserveAdditionalId(imv);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }
    }

}
