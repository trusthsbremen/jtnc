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
package org.ietf.nea.pt.serialize.writer.bytebuffer;

import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;

/**
 * Factory utility to create a writer, that can serialize an entire transport
 * message compliant to RFC 6876 from a Java object to a buffer of bytes.
 *
 */
public abstract class PtTlsWriterFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PtTlsWriterFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 6876 protocol supported by a writer,
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
     * elements, that are specified by RFC 6876 and allowed in a production
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
     * elements, that are specified by RFC 6876 including the experimental
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
