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
package org.ietf.nea.pt.serialize.reader.bytebuffer;

import org.ietf.nea.pt.message.PtTlsMessageHeaderBuilderIetf;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.value.PtTlsMessageValueErrorBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueExperimentalBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValuePbBatchBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationDataBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelectionBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismsBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueSaslResultBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequestBuilderIetf;
import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponseBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;

/**
 * Factory utility to create a reader, that can parse an entire transport
 * message compliant to RFC 6876 from a buffer of bytes to a Java object.
 *
 */
public final class PtTlsReaderFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PtTlsReaderFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 6876 protocol supported by a reader,
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgTProtocolBindingEnum.TLS1;
    }

    /**
     * Creates a reader to parse an entire transport message compliant to
     * RFC 6876 from a buffer of bytes to a Java object.
     * The reader supports all elements, that are specified by RFC 5793 and
     * allowed in a production environment.
     *
     * @return the transport message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportReader<TransportMessage>
        createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PtTlsMessageHeaderReader mReader = new PtTlsMessageHeaderReader(
                new PtTlsMessageHeaderBuilderIetf());

        PtTlsReader reader = new PtTlsReader(mReader);

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.id(),
                (TransportReader) new PtTlsMessageVersionRequestValueReader(
                        new PtTlsMessageValueVersionRequestBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.id(),
                (TransportReader) new PtTlsMessageVersionResponseValueReader(
                        new PtTlsMessageValueVersionResponseBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id(),
                (TransportReader) new PtTlsMessagePbBatchValueReader(
                        new PtTlsMessageValuePbBatchBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id(),
                (TransportReader) new PtTlsMessageErrorValueReader(
                        new PtTlsMessageValueErrorBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.id(),
                (TransportReader) new PtTlsMessageSaslMechanismsValueReader(
                        new PtTlsMessageValueSaslMechanismsBuilderIetf()));
        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.id(),
                (TransportReader) new PtTlsMessageSaslMechanismSelectionValueReader(
                        new PtTlsMessageValueSaslMechanismSelectionBuilderIetf()));
        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id(),
                (TransportReader) new PtTlsMessageSaslAuthenticationDataValueReader(
                        new PtTlsMessageValueSaslAuthenticationDataBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id(),
                (TransportReader) new PtTlsMessageSaslResultValueReader(
                        new PtTlsMessageValueSaslResultBuilderIetf()));

        return reader;
    }

    /**
     * Creates a reader to parse an entire transport message compliant to
     * RFC 6876 from a buffer of bytes to a Java object.
     * The reader supports all elements, that are specified by RFC 6876
     * including the experimental message value.
     *
     * @return the transport message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportReader<TransportMessage>
        createExperimentalDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PtTlsReader reader = (PtTlsReader) createProductionDefault();

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.id(),
                (TransportReader) new PtTlsMessageExperimentalValueReader(
                        new PtTlsMessageValueExperimentalBuilderIetf()));

        return reader;
    }

}
