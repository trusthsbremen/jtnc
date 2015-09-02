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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * IMC connection adapter adapting an IMC connection according to
 * IETF/TCG specifications, that supports long addressing.
 *
 *
 */
class ImcConnectionAdapterIetfLong extends ImcConnectionAdapterIetf implements
        IMCConnectionLong {

    /**
     * Creates an IMC connection adapter with long addressing support for
     * an IMC with the given ID and the given IMC connection context.
     *
     * @param primaryImcId the IMC ID
     * @param context the connection context
     */
    ImcConnectionAdapterIetfLong(final int primaryImcId,
            final ImcConnectionContext context) {
        super(primaryImcId, context);
    }

    @Override
    public void sendMessageLong(final long messageFlags,
            final long messageVendorID, final long messageSubtype,
            final byte[] message, final long sourceIMCID,
            final long destinationIMVID) throws TNCException {

        if (messageVendorID == TNCConstants.TNC_VENDORID_ANY
                || messageSubtype == TNCConstants.TNC_SUBTYPE_ANY) {

            throw new TNCException("Message type is set to reserved type.",
                    TNCException.TNC_RESULT_INVALID_PARAMETER);
        }

        if (!super.isReceiving()) {
            throw new TNCException(
                    "Connection is currently not allowed to receive messages.",
                    TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.id());
        }

        super.checkMessageSize(message.length);

        TnccsMessage m = null;
        try {
            m = PbMessageFactoryIetf.createIm(new PbMessageImFlagEnum[0],
                    messageVendorID, messageSubtype, (int) sourceIMCID,
                    (int) destinationIMVID, message);

        } catch (ValidationException e) {
            throw new TNCException(e.getCause().getMessage(), e.getCause()
                    .getErrorCode());
        }

        try {
            super.sendMessage(m);
        } catch (TncException e) {
            throw new TNCException(e.getMessage(), e.getResultCode().id());
        }

    }

}
