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
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;

/**
 * Factory utility to create a reader that can parse an entire transport message
 * compliant to RFC 6876 from a buffer of bytes to a Java object.
 *
 * @author Carl-Heinz Genzel
 */
public abstract class PtTlsReaderFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PtTlsReaderFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 6876 protocol supported by a reader
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
     * The reader supports all elements that are specified by RFC 5793 and
     * allowed in a production environment.
     *
     * @return the transport message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportReader<TransportMessageContainer>
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
     * The reader supports all elements that are specified by RFC 6876 including
     * the experimental message value.
     *
     * @return the transport message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TransportReader<TransportMessageContainer>
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
