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
package org.ietf.nea.pa.serialize.writer.bytebuffer;

import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.m.enums.TcgMProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;

/**
 * Factory utility to create a writer, that can serialize an entire integrity
 * measurement component message compliant to RFC 5792 from a Java object to a
 * buffer of bytes.
 *
 */
public final class PaWriterFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PaWriterFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 5792 protocol supported by a writer,
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgMProtocolBindingEnum.M1;
    }

    /**
     * Creates a writer to serialize an entire integrity measurement component
     * message compliant to RFC 5792 from a Java object to a buffer of bytes.
     * The writer supports all elements, that are specified by RFC 5792 and
     * allowed in a production environment.
     *
     * @return the integrity measurement component message writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ImWriter<ImMessage> createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PaAttributeHeaderWriter aWriter = new PaAttributeHeaderWriter();

        PaMessageHeaderWriter mWriter = new PaMessageHeaderWriter();

        PaWriter writer = new PaWriter(mWriter, aWriter);

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(),
                (ImWriter) new PaAttributeAssessmentResultValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id(),
                (ImWriter) new PaAttributeAttributeRequestValueWriter());
        writer.add(
                IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED.id(),
                (ImWriter) new PaAttributeFactoryDefaultPasswordEnabledValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED.id(),
                (ImWriter) new PaAttributeForwardingEnabledValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.id(),
                (ImWriter) new PaAttributeInstalledPackagesValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(),
                (ImWriter) new PaAttributeNumericVersionValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS.id(),
                (ImWriter) new PaAttributeOperationalStatusValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_PORT_FILTER.id(),
                (ImWriter) new PaAttributePortFilterValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.id(),
                (ImWriter) new PaAttributeProductInformationValueWriter());
        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id(),
                (ImWriter) new PaAttributeStringVersionValueWriter());

        writer.add(
                IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ERROR.id(),
                (ImWriter) new PaAttributeErrorValueWriter(
                    new PaAttributeErrorInformationInvalidParamValueWriter(),
                    new PaAttributeErrorInformationUnsupportedVersionValueWriter(),
                    new PaAttributeErrorInformationUnsupportedAttributeValueWriter()));

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS.id(),
                (ImWriter) new PaAttributeRemediationParametersValueWriter(
                    new PaAttributeRemediationParameterStringValueWriter(),
                    new PaAttributeRemediationParameterUriValueWriter()));

        return writer;
    }

    /**
     * Creates a writer to serialize an entire integrity measurement component
     * message compliant to RFC 5792 from a Java object to a buffer of bytes.
     * The writer supports all elements, that are specified by RFC 5792 including
     * the test attribute value.
     *
     * @return the integrity measurement component message writer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ImWriter<ImMessage> createTestingDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PaWriter writer = (PaWriter) createProductionDefault();

        writer.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_TESTING.id(),
                (ImWriter) new PaAttributeTestingValueWriter());

        return writer;

    }

}
