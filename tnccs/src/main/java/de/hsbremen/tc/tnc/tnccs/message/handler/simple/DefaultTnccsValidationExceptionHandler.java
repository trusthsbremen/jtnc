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
package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;

/**
 * Default handler to handle minor message validation
 * exceptions.
 *
 * @author Carl-Heinz Genzel
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
        if (attributes == null) {
            throw new NullPointerException(
                    "Attributes cannot be null. Use empty attributes instead.");
        }
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
                                PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION,
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
