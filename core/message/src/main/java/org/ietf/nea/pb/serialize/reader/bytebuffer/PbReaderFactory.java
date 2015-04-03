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
package org.ietf.nea.pb.serialize.reader.bytebuffer;

import org.ietf.nea.pb.batch.PbBatchHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageHeaderBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueExperimentalBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueImBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreferenceBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueReasonStringBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.message.util
.PbMessageValueErrorParameterOffsetBuilderIetf;
import org.ietf.nea.pb.message.util
.PbMessageValueErrorParameterVersionBuilderIetf;
import org.ietf.nea.pb.message.util
.PbMessageValueRemediationParameterStringBuilderIetf;
import org.ietf.nea.pb.message.util
.PbMessageValueRemediationParameterUriBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;

/**
 * Factory utility to create a reader, that can parse an entire TNCCS batch
 * compliant to RFC 5793 from a buffer of bytes to a Java object.
 *
 */
public final class PbReaderFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PbReaderFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 5793 protocol supported by a reader,
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgTnccsProtocolBindingEnum.TNCCS2;
    }

    /**
     * Creates a reader to parse an entire TNCCS batch compliant to
     * RFC 5793 from a buffer of bytes to a Java object.
     * The reader supports all elements, that are specified by RFC 5793 and
     * allowed in a production environment.
     *
     * @return the TNCCS batch reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TnccsReader<TnccsBatchContainer> createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PbMessageHeaderReader mReader = new PbMessageHeaderReader(
                new PbMessageHeaderBuilderIetf());

        PbBatchHeaderReader bReader = new PbBatchHeaderReader(
                new PbBatchHeaderBuilderIetf());

        PbReader reader = new PbReader(bReader, mReader);

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_PA.id(),
                (TnccsReader) new PbMessageImValueReader(
                        new PbMessageValueImBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
                (TnccsReader) new PbMessageReasonStringValueReader(
                        new PbMessageValueReasonStringBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id(),
                (TnccsReader) new PbMessageAccessRecommendationValueReader(
                        new PbMessageValueAccessRecommendationBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id(),
                (TnccsReader) new PbMessageAssessmentResultValueReader(
                        new PbMessageValueAssessmentResultBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id(),
                (TnccsReader) new PbMessageLanguagePreferenceValueReader(
                        new PbMessageValueLanguagePreferenceBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_PA.id(),
                (TnccsReader) new PbMessageImValueReader(
                        new PbMessageValueImBuilderIetf()));
        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REASON_STRING.id(),
                (TnccsReader) new PbMessageReasonStringValueReader(
                        new PbMessageValueReasonStringBuilderIetf()));

        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ERROR.id(),
                (TnccsReader) new PbMessageErrorValueReader(
                      new PbMessageValueErrorBuilderIetf(),
                      new PbMessageErrorParameterOffsetValueReader(
                           new PbMessageValueErrorParameterOffsetBuilderIetf()),
                      new PbMessageErrorParameterVersionValueReader(
                        new PbMessageValueErrorParameterVersionBuilderIetf())));

        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.id(),
                (TnccsReader) new PbMessageRemediationParametersValueReader(
                   new PbMessageValueRemediationParametersBuilderIetf(),
                   new PbMessageRemediationParameterStringValueReader(
                     new PbMessageValueRemediationParameterStringBuilderIetf()),
                   new PbMessageRemediationParameterUriValueReader(
                     new PbMessageValueRemediationParameterUriBuilderIetf())));

        return reader;
    }

    /**
     * Creates a reader to parse an entire TNCCS batch compliant to
     * RFC 5793 from a buffer of bytes to a Java object.
     * The reader supports all elements, that are specified by RFC 5793
     * including the experimental message value.
     *
     * @return the TNCCS batch reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TnccsReader<TnccsBatchContainer> createExperimentalDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PbReader reader = (PbReader) createProductionDefault();

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.id(),
                (TnccsReader) new PbMessageExperimentalValueReader(
                        new PbMessageValueExperimentalBuilderIetf()));

        return reader;
    }

}
