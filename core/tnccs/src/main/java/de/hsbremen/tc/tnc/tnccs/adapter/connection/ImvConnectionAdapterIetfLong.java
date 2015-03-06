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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnectionLong;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * IMV connection adapter adapting an IMV connection according to
 * IETF/TCG specifications, that supports long addressing.
 *
 *
 */
class ImvConnectionAdapterIetfLong extends ImvConnectionAdapterIetf implements
        IMVConnectionLong {

    /**
     * Creates an IMV connection adapter with long addressing support for
     * an IMV with the given ID and the given IMV connection context.
     *
     * @param primaryImvId the IMV ID
     * @param context the connection context
     */
    ImvConnectionAdapterIetfLong(final int primaryImvId,
            final ImvConnectionContext context) {
        super(primaryImvId, context);
    }

    @Override
    public void sendMessageLong(final long messageFlags,
            final long messageVendorID, final long messageSubtype,
            final byte[] message, final long sourceIMVID,
            final long destinationIMCID) throws TNCException {

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
                    messageVendorID, messageSubtype, (int) destinationIMCID,
                    (int) sourceIMVID, message);
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
