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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * Utility to create a single message for an error or a close
 * batch containing error messages if the connection has to be closed
 * because of an error.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class StateUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(StateUtil.class);

    /**
     * Private constructor should never be invoked.
     */
    private StateUtil() {
        throw new AssertionError();
    }

    /**
     * Creates a message batch with the given error messages
     * to close the connection.
     * @param server true if this side is a TNCS
     * @param error the error messages
     * @return the message batch
     */
    public static TnccsBatch createCloseBatch(final boolean server,
            final TnccsMessage... error) {

        List<TnccsMessage> messages = (error != null) ? Arrays.asList(error)
                : new ArrayList<TnccsMessage>();
        TnccsBatch b = null;
        try {
            if (server) {
                b = PbBatchFactoryIetf.createServerClose(messages);
            } else {
                b = PbBatchFactoryIetf.createClientClose(messages);
            }
        } catch (ValidationException e) {
            LOGGER.error("Error while creating a close batch, "
                    + "transitioning to end state, without close.");
        }

        return b;
    }

    /**
     * Creates an error message to signal an unexpected state error.
     *
     * @return the error message
     */
    public static TnccsMessage createUnexpectedStateError() {
        try {
            return PbMessageFactoryIetf
                    .createErrorSimple(
                            new PbMessageErrorFlagsEnum[] {
                                    PbMessageErrorFlagsEnum.FATAL},
                            PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE);
        } catch (ValidationException e) {
            return null;
        }
    }

    /**
     * Creates an error message to signal an unsupported version error.
     *
     * @param badVersion the unsupported  version
     * @param minVersion the minimal supported version
     * @param maxVersion the maximal supported version
     * @return the error message
     */
    public static TnccsMessage createUnsupportedVersionError(
            final short badVersion,
            final short minVersion, final short maxVersion) {
        try {
            return PbMessageFactoryIetf
                    .createErrorVersion(
                            new PbMessageErrorFlagsEnum[] {
                                    PbMessageErrorFlagsEnum.FATAL},
                            badVersion, maxVersion, minVersion);
        } catch (ValidationException e) {
            return null;
        }
    }

    /**
     * Creates an error message to signal a local error.
     *
     * @return the error message
     */
    public static TnccsMessage createLocalError() {
        try {
            return PbMessageFactoryIetf
                    .createErrorSimple(
                            new PbMessageErrorFlagsEnum[] {
                                    PbMessageErrorFlagsEnum.FATAL},
                            PbMessageErrorCodeEnum.IETF_LOCAL);
        } catch (ValidationException e) {
            return null;
        }
    }
}
