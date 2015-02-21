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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Default exception handler.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultImValueExceptionHandler implements ImValueExceptionHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultImValueExceptionHandler.class);
    private static final short MAX_VERSION = 1;
    private static final short MIN_VERSION = 1;

    @Override
    public List<ImAttribute> handle(final ImFaultyObjectComponent component) {
        byte[] messageHeader =
                (component.getMessageHeader() != null)
                ? component.getMessageHeader() : new byte[0];

        List<ValidationException> exceptions =
                (component.getExceptions() != null)
                ? component.getExceptions()
                : new ArrayList<ValidationException>(0);

        List<ImAttribute> errorAttributes = new ArrayList<>();
        for (ValidationException validationException : exceptions) {
            RuleException ruleEx = validationException.getCause();
            ImAttribute error = null;

            try {
                if (ruleEx.getErrorCode()
                        == PaAttributeErrorCodeEnum
                        .IETF_INVALID_PARAMETER.code()) {

                    error = PaAttributeFactoryIetf
                            .createErrorInformationInvalidParam(messageHeader,
                                    validationException.getExceptionOffset());

                } else if (ruleEx.getErrorCode()
                        == PaAttributeErrorCodeEnum
                        .IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()) {

                    List<Object> reasons = validationException.getReasons();

                    PaAttributeHeader attributeHeader = null;
                    for (Object object : reasons) {
                        if (object instanceof PaAttributeHeader) {
                            attributeHeader = (PaAttributeHeader) object;
                        }
                    }

                    error = PaAttributeFactoryIetf
                            .createErrorInformationUnsupportedAttribute(
                                    messageHeader, attributeHeader);

                } else if (ruleEx.getErrorCode()
                        == PaAttributeErrorCodeEnum
                        .IETF_UNSUPPORTED_VERSION.code()) {

                    error = PaAttributeFactoryIetf
                            .createErrorInformationUnsupportedVersion(
                                    messageHeader, MAX_VERSION, MIN_VERSION);
                }
            } catch (ValidationException e) {
                LOGGER.error(
                        "Could not create error attribute, "
                        + "because a value error exists.",
                        e);
            }

            if (error != null) {
                errorAttributes.add(error);
            }
        }

        return errorAttributes;

    }

}
