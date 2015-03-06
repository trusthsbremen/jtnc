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
package org.ietf.nea.pb.serialize.writer.bytebuffer;

import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;

/**
 * Factory utility to create a writer that can serialize an entire TNCCS batch
 * compliant to RFC 5793 from a Java object to a buffer of bytes.
 *
 * @author Carl-Heinz Genzel
 */
public abstract class PbWriterFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PbWriterFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 5793 protocol supported by a writer
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgTnccsProtocolBindingEnum.TNCCS2;
    }

    /**
     * Creates a writer to serialize an entire TNCCS batch compliant to RFC 5793
     * from a Java object to a buffer of bytes. The writer supports all elements
     * that are specified by RFC 5793 and allowed in a production environment.
     *
     * @return the TNCCS batch writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TnccsWriter<TnccsBatch> createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PbMessageHeaderWriter mWriter = new PbMessageHeaderWriter();

        PbBatchHeaderWriter bWriter = new PbBatchHeaderWriter();

        PbWriter writer = new PbWriter(bWriter, mWriter);

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_PA.id(),
                (TnccsWriter) new PbMessageImValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
                (TnccsWriter) new PbMessageReasonStringValueWriter());

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id(),
                (TnccsWriter) new PbMessageAccessRecommendationValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id(),
                (TnccsWriter) new PbMessageAssessmentResultValueWriter());

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id(),
                (TnccsWriter) new PbMessageLanguagePreferenceValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_PA.id(),
                (TnccsWriter) new PbMessageImValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
                (TnccsWriter) new PbMessageReasonStringValueWriter());

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ERROR.id(),
                (TnccsWriter) new PbMessageErrorValueWriter(
                        new PbMessageErrorParameterOffsetValueWriter(),
                        new PbMessageErrorParameterVersionValueWriter()));

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.id(),
                (TnccsWriter) new PbMessageRemediationParametersValueWriter(
                        new PbMessageRemediationParameterStringValueWriter(),
                        new PbMessageRemediationParameterUriValueWriter()));

        return writer;
    }

    /**
     * Creates a writer to serialize an entire TNCCS batch compliant to RFC 5793
     * from a Java object to a buffer of bytes. The writer supports all elements
     * that are specified by RFC 5793 including the experimental message value.
     *
     * @return the TNCCS batch writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TnccsWriter<TnccsBatch> createExperimentalDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PbWriter writer = (PbWriter) createProductionDefault();

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.id(),
                (TnccsWriter) new PbMessageExperimentalValueWriter());

        return writer;

    }

}
