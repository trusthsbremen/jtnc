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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

/**
 * Default TNCS state helper to make information and commonly used
 * functions accessible to a TNCS state.
 *
 *
 */
public class DefaultServerStateHelper extends
        AbstractStateHelper<TncsContentHandler> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultServerStateHelper.class);

    /**
     * Creates a default TNCC state helper with the given session
     * and/or connection attributes, which should be accessible by a state and
     * a TNCS content handler, that provides message handling functions.
     *
     * @param attributes the session/connection attributes
     * @param contentHandler the content handler
     */
    public DefaultServerStateHelper(final Attributed attributes,
            final TncsContentHandler contentHandler) {
        super(attributes, contentHandler);
    }

    @Override
    public State getState(final TnccsStateEnum id) {

        State t = null;
        switch (id) {
        case CLIENT_WORKING:
            t = new DefaultServerClientWorkingState(this);
            break;
        case DECIDED:
            t = new DefaultServerDecidedState(this);
            break;
        case END:
            t = new DefaultCommonEndState(true, this);
            break;
        case ERROR:
            t = new DefaultCommonErrorState(true, this);
            break;
        case INIT:
            t = new DefaultServerInitState(this);
            break;
        case RETRY:
            t = new DefaultServerRetryState(this);
            break;
        case SERVER_WORKING:
            t = new DefaultServerServerWorkingState(this);
            break;
        default:
            throw new IllegalArgumentException(
                    "The implementation of the state with id " + id.value()
                            + " is unknown.");
        }

        LOGGER.debug(t.getClass().getCanonicalName() + " created.");
        return t;

    }
}
