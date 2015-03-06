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
