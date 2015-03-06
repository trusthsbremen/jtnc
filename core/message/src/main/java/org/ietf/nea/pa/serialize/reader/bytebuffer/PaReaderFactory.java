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
package org.ietf.nea.pa.serialize.reader.bytebuffer;

import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResultBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequestBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueErrorBuilderIetf;
import org.ietf.nea.pa.attribute
.PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabledBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackagesBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersionBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatusBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValuePortFilterBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformationBuilderIetf;
import org.ietf.nea.pa.attribute
.PaAttributeValueRemediationParametersBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersionBuilderIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueTestingBuilderIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationInvalidParamBuilderIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterStringBuilderIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterUriBuilderIetf;
import org.ietf.nea.pa.message.PaMessageHeaderBuilderIetf;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;
import de.hsbremen.tc.tnc.message.m.enums.TcgMProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;

/**
 * Factory utility to create a reader that can parse an entire integrity
 * measurement component message compliant to RFC 5792 from a buffer of bytes to
 * a Java object.
 *
 * @author Carl-Heinz Genzel
 */
public abstract class PaReaderFactory {

    /**
     * Private constructor should never be invoked.
     */
    private PaReaderFactory() {
        throw new AssertionError();
    }

    /**
     * Returns the identifier of the RFC 5792 protocol supported by a reader
     * that is created with this factory.
     *
     * @return the protocol identifier
     */
    public static TcgProtocolBindingIdentifier getProtocolIdentifier() {
        return TcgMProtocolBindingEnum.M1;
    }

    /**
     * Creates a reader to parse an entire integrity measurement component
     * message compliant to RFC 5792 from a buffer of bytes to a Java object.
     * The reader supports all elements that are specified by RFC 5792 and
     * allowed in a production environment.
     *
     * @return the integrity measurement component message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ImReader<ImMessageContainer> createProductionDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PaMessageHeaderReader mReader = new PaMessageHeaderReader(
                new PaMessageHeaderBuilderIetf());

        PaAttributeHeaderReader aReader = new PaAttributeHeaderReader(
                new PaAttributeHeaderBuilderIetf());

        PaReader reader = new PaReader(mReader, aReader);

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(),
                (ImReader) new PaAttributeAssessmentResultValueReader(
                        new PaAttributeValueAssessmentResultBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id(),
                (ImReader) new PaAttributeAttributeRequestValueReader(
                        new PaAttributeValueAttributeRequestBuilderIetf()));

        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED.id(),
           (ImReader) new PaAttributeFactoryDefaultPasswordEnabledValueReader(
             new PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED.id(),
                (ImReader) new PaAttributeForwardingEnabledValueReader(
                        new PaAttributeValueForwardingEnabledBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.id(),
                (ImReader) new PaAttributeInstalledPackagesValueReader(
                        new PaAttributeValueInstalledPackagesBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(),
                (ImReader) new PaAttributeNumericVersionValueReader(
                        new PaAttributeValueNumericVersionBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS.id(),
                (ImReader) new PaAttributeOperationalStatusValueReader(
                        new PaAttributeValueOperationalStatusBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_PORT_FILTER.id(),
                (ImReader) new PaAttributePortFilterValueReader(
                        new PaAttributeValuePortFilterBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.id(),
                (ImReader) new PaAttributeProductInformationValueReader(
                        new PaAttributeValueProductInformationBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id(),
                (ImReader) new PaAttributeStringVersionValueReader(
                        new PaAttributeValueStringVersionBuilderIetf()));

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id(),
                (ImReader) new PaAttributeAttributeRequestValueReader(
                        new PaAttributeValueAttributeRequestBuilderIetf()));

        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS.id(),
                (ImReader) new PaAttributeRemediationParametersValueReader(
                        new PaAttributeValueRemediationParametersBuilderIetf(),
                        new PaAttributeRemediationParameterStringValueReader(
                                new PaAttributeValueRemediationParameterStringBuilderIetf()),
                        new PaAttributeRemediationParameterUriValueReader(
                                new PaAttributeValueRemediationParameterUriBuilderIetf())));

        reader.add(
                IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_ERROR.id(),
                (ImReader) new PaAttributeErrorValueReader(
                        new PaAttributeValueErrorBuilderIetf(),
                        new PaAttributeErrorInformationInvalidParamValueReader(
                                new PaAttributeValueErrorInformationInvalidParamBuilderIetf()),
                        new PaAttributeErrorInformationUnsupportedVersionValueReader(
                                new PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf()),
                        new PaAttributeErrorInformationUnsupportedAttributeValueReader(
                                new PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf(),
                                new PaAttributeHeaderBuilderIetf())));

        return reader;
    }

    /**
     * Creates a reader to parse an entire integrity measurement component
     * message compliant to RFC 5792 from a buffer of bytes to a Java object.
     * The reader supports all elements that are specified by RFC 5792 including
     * the test attribute value.
     *
     * @return the integrity measurement component message reader
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ImReader<ImMessageContainer> createTestingDefault() {

        /*
         * TODO Remove raw types and unchecked conversion. Unfortunately I could
         * not find a way around using raw types and unchecked conversion my be
         * some one else can.
         */

        PaReader reader = (PaReader) createProductionDefault();

        reader.add(IETFConstants.IETF_PEN_VENDORID,
                PaAttributeTypeEnum.IETF_PA_TESTING.id(),
                (ImReader) new PaAttributeTestingValueReader(
                        new PaAttributeValueTestingBuilderIetf()));

        return reader;
    }
}
