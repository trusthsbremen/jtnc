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
