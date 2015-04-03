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
 *
 */
public final class StateUtil {

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
