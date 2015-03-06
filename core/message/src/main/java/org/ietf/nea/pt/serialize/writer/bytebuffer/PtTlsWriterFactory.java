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
package org.ietf.nea.pt.serialize.writer.bytebuffer;

import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;

/**
 * Factory utility to create a writer that can serialize an entire transport
 * message compliant to RFC 6876 from a Java object to a buffer of bytes.
 *
 * @author Carl-Heinz Genzel
 */
public abstract class PtTlsWriterFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PtTlsWriterFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 6876 protocol supported by a writer
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgTProtocolBindingEnum.TLS1;
    }

    /**
     * Creates a writer to serialize an entire transport message compliant to
     * RFC 6876 from a Java object to a buffer of bytes. The writer supports all
     * elements that are specified by RFC 6876 and allowed in a production
     * environment.
     *
     * @return the transport message writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportWriter<TransportMessage> createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PtTlsMessageHeaderWriter mWriter = new PtTlsMessageHeaderWriter();

        PtTlsWriter writer = new PtTlsWriter(mWriter);

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.id(),
                (TransportWriter) new PtTlsMessageVersionRequestValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.id(),
                (TransportWriter) new PtTlsMessageVersionResponseValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id(),
                (TransportWriter) new PtTlsMessagePbBatchValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(),
                (TransportWriter) new PtTlsMessageErrorValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.id(),
                (TransportWriter) new PtTlsMessageSaslMechanismsValueWriter());
        writer.add(
                IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.id(),
        (TransportWriter) new PtTlsMessageSaslMechanismSelectionValueWriter());
        writer.add(
                IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id(),
        (TransportWriter) new PtTlsMessageSaslAutenticationDataValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id(),
                (TransportWriter) new PtTlsMessageSaslResultValueWriter());

        return writer;
    }

    /**
     * Creates a writer to serialize an entire transport message compliant to
     * RFC 6876 from a Java object to a buffer of bytes. The writer supports all
     * elements that are specified by RFC 6876 including the experimental
     * message value.
     *
     * @return the Ttransport message writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportWriter<TransportMessage>
        createExperimentalDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PtTlsWriter writer = (PtTlsWriter) createProductionDefault();

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.id(),
                (TransportWriter) new PtTlsMessageExperimentalValueWriter());

        return writer;

    }

}
