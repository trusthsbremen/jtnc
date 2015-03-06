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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Default exception handler.
 *
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
