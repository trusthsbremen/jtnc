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
package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default handler to handle minor message validation
 * exceptions.
 *
 *
 */
public class DefaultTnccsValidationExceptionHandler implements
        TnccsValidationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultTnccsValidationExceptionHandler.class);

    // TODO This is not used yet, but may be good for other versions.
    @SuppressWarnings("unused")
    private final Attributed attributes;

    /**
     * Creates a validation exception handler with the given
     * accessible session and/or connection attributes.
     *
     * @param attributes the session/connection attributes
     */
    public DefaultTnccsValidationExceptionHandler(final Attributed attributes) {
        NotNull.check("Attributes cannot be null. Use empty attributes instead."
                , attributes);

        this.attributes = attributes;
    }

    @Override
    public List<TnccsMessage> handle(
            final List<ValidationException> exceptions) {
        List<TnccsMessage> errorMessages = new LinkedList<>();
        if (exceptions != null) {

            for (ValidationException validationException : exceptions) {
                LOGGER.warn(
                        "An exception was discovered and an "
                        + "error message will be created. ",
                        validationException);

                TnccsMessage message = this.createPbError(validationException);
                if (message != null) {
                    errorMessages.add(message);
                }
            }
        }
        return errorMessages;
    }

    @Override
    public void dump(final List<ValidationException> exceptions) {
        if (exceptions != null) {

            for (ValidationException validationException : exceptions) {
                LOGGER.warn(
                        "An exception was discovered but no error message "
                        + "will be created, because exception was dumped. ",
                        validationException);
            }
        }
    }

    /**
     * Creates an error message describing the validation
     * exception.
     *
     * @param exception the validation exception
     * @return the error message
     */
    private TnccsMessage createPbError(final ValidationException exception) {

        long i = exception.getCause().getErrorCode();

        try {

            if (i == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code()) {

                return PbMessageFactoryIetf
                        .createErrorOffset(
                                new PbMessageErrorFlagsEnum[]
                                        {PbMessageErrorFlagsEnum.FATAL},
                                PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER,
                                exception.getExceptionOffset());

            } else if (i == PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE
                    .code()) {

                return PbMessageFactoryIetf
                        .createErrorSimple(
                                new PbMessageErrorFlagsEnum[]
                                        {PbMessageErrorFlagsEnum.FATAL},
                                PbMessageErrorCodeEnum
                                    .IETF_UNEXPECTED_BATCH_TYPE);

            } else if (i == PbMessageErrorCodeEnum
                    .IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()) {

                return PbMessageFactoryIetf
                        .createErrorOffset(
                                new PbMessageErrorFlagsEnum[]
                                        {PbMessageErrorFlagsEnum.FATAL},
                                PbMessageErrorCodeEnum
                                    .IETF_UNSUPPORTED_MANDATORY_MESSAGE,
                                exception.getExceptionOffset());

            } else if (i == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION
                    .code()) {
                RuleException e = exception.getCause();
                // parsing the reasons using the conventions in the batch
                // version check class of messages.
                short actual = 0;
                short maxVersion = 0;
                short minVersion = 0;
                if (e.getReasons() != null) {
                    int j = 0;
                    for (Object o : e.getReasons()) {
                        if (o instanceof Short) {
                            switch (j) {
                            case 0:
                                actual = ((Short) o).shortValue();
                                i++;
                                break;
                            case 1:
                                maxVersion =  ((Short) o).shortValue();
                                minVersion = maxVersion;
                                i++;
                                break;
                            case 2:
                                minVersion = ((Short) o).shortValue();
                                i++;
                                break;
                            default:
                                // nothing to to here
                                break;
                            }
                        }
                    }
                }
                return PbMessageFactoryIetf
                        .createErrorVersion(
                                new PbMessageErrorFlagsEnum[]
                                        {PbMessageErrorFlagsEnum.FATAL},
                                actual, maxVersion, minVersion);

            } else {
                // defaults to PbMessageErrorCodeEnum.IETF_LOCAL
                return PbMessageFactoryIetf
                        .createErrorSimple(
                                new PbMessageErrorFlagsEnum[]
                                        {PbMessageErrorFlagsEnum.FATAL},
                                PbMessageErrorCodeEnum.IETF_LOCAL);
            }
        } catch (ValidationException e) {
            LOGGER.error("Could not create error message for exception \""
                    + e.getMessage()
                    + "\". Because this should never happen, "
                    + "the error message is not send.");
            return null;
        }
    }

}
