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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
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
 * @author Carl-Heinz Genzel
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
            m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0],
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
