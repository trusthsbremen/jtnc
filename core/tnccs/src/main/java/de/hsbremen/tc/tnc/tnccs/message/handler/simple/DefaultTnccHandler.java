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
package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default handler to handle messages destined to/from a TNCC.
 *
 *
 */
public class DefaultTnccHandler implements TnccHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultTnccHandler.class);

    private final Attributed sessionAttributes;
    private String tnccLanguagePreference;

    private String tempTnccsLanguagePreference;
    private TncConnectionState state;
    private boolean handshakeStartet;

    /**
     * Creates a TNCC message handler with the given
     * accessible session and/or connection attributes.
     *
     * @param sessionAttributes the session/connection attributes
     */
    public DefaultTnccHandler(final Attributed sessionAttributes) {
        NotNull.check("Attributes cannot be null. Use empty attributes instead."
                , sessionAttributes);

        this.sessionAttributes = sessionAttributes;
        this.state = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
        this.tnccLanguagePreference = HSBConstants.HSB_DEFAULT_LANGUAGE;
        this.handshakeStartet = false;
    }

    /**
     * Returns the accessible session and/or connection attributes.
     *
     * @return the sessionAttributes the session/connection attributes
     */
    protected Attributed getAttributes() {
        return this.sessionAttributes;
    }

    /**
     * Returns the preferred language for human readable message
     * content.
     *
     * @return the tnccLanguagePreference
     */
    protected String getTnccLanguagePreference() {
        return this.tnccLanguagePreference;
    }

    @Override
    public TncConnectionState getAccessDecision() {
        return this.state;
    }

    @Override
    public void setConnectionState(final TncConnectionState state) {
        LOGGER.debug("Connection state has changed to " + state.toString()
                + ".");
        this.state = state;
        if (this.state.equals(DefaultTncConnectionStateEnum
                        .TNC_CONNECTION_STATE_HANDSHAKE)) {
            this.handshakeStartet = true;
        }

    }

    @Override
    public List<TnccsMessage> requestMessages() {
        List<TnccsMessage> messages = new ArrayList<>();
        if (handshakeStartet) {
            TnccsMessage message = null;
            try {
                message = PbMessageFactoryIetf
                        .createLanguagePreference(this.tnccLanguagePreference);
                messages.add(message);
            } catch (ValidationException e) {
                LOGGER.warn(
                        "TnccsMessage with language preference could not "
                        + "be created and will be ignored.",
                        e);
            }
            this.handshakeStartet = false;
        }

        return messages;

    }

    @Override
    public List<TnccsMessage> forwardMessage(final TnccsMessage message) {

        if (message == null || message.getValue() == null) {
            LOGGER.debug("Because message or message value is null, "
                    + "it is ignored.");
            return new ArrayList<TnccsMessage>();
        }

        TnccsMessageValue value = message.getValue();

        LOGGER.debug("Message value received: " + value.toString());

        if (value instanceof PbMessageValueAccessRecommendation) {
            this.handleRecommendation(
                    (PbMessageValueAccessRecommendation) value);
        } else if (value instanceof PbMessageValueLanguagePreference) {
            this.handleLanguagePreference(
                    (PbMessageValueLanguagePreference) value);
        }

        List<TnccsMessage> messages = new ArrayList<>();
        if (handshakeStartet) {
            TnccsMessage m = null;
            try {
                m = PbMessageFactoryIetf
                        .createLanguagePreference(this.tnccLanguagePreference);
                messages.add(m);
            } catch (ValidationException e) {
                LOGGER.warn(
                        "TnccsMessage with language preference could not be "
                        + "created and will be ignored.",
                        e);
            }
            this.handshakeStartet = false;
        }
        return messages;
    }

    @Override
    public void dumpMessage(final TnccsMessage message) {

        if (message == null || message.getValue() == null) {
            LOGGER.debug("Because Message or message value is null, "
                    + "it is ignored.");
            return;
        }

        TnccsMessageValue value = message.getValue();

        LOGGER.debug("Message value received: " + value.toString());

        if (value instanceof PbMessageValueAccessRecommendation) {
            this.handleRecommendation(
                    (PbMessageValueAccessRecommendation) value);
        } else if (value instanceof PbMessageValueLanguagePreference) {
            this.handleLanguagePreference(
                    (PbMessageValueLanguagePreference) value);
        }
    }

    /**
     * Handles a message containing the preferred language for
     * human readable strings of a peer.
     *
     * @param value the message
     */
    private void handleLanguagePreference(
            final PbMessageValueLanguagePreference value) {

        String lang = value.getPreferedLanguage();

        if (lang != null && !lang.isEmpty()) {
            this.tempTnccsLanguagePreference = lang.trim();
        }
    }

    /**
     * Handles a message containing the integrity check handshake result.
     *
     * @param value the message
     */
    private void handleRecommendation(
            final PbMessageValueAccessRecommendation value) {
        PbMessageAccessRecommendationEnum rec = value.getRecommendation();
        switch (rec) {
        case ALLOWED:
            this.setConnectionState(DefaultTncConnectionStateEnum
                    .TNC_CONNECTION_STATE_ACCESS_ALLOWED);
            break;
        case DENIED:
            this.setConnectionState(DefaultTncConnectionStateEnum
                    .TNC_CONNECTION_STATE_ACCESS_NONE);
            break;
        case QURANTINED:
            this.setConnectionState(DefaultTncConnectionStateEnum
                    .TNC_CONNECTION_STATE_ACCESS_ISOLATED);
            break;
        default:
            this.setConnectionState(DefaultTncConnectionStateEnum
                    .TNC_CONNECTION_STATE_ACCESS_NONE);
            break;
        }
    }

    @Override
    public List<TnccsMessage> lastCall() {
        if (this.tempTnccsLanguagePreference != null) {
            try {
                this.sessionAttributes
                        .setAttribute(
                                TncCommonAttributeTypeEnum
                                .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE,
                                this.tempTnccsLanguagePreference);
                this.tnccLanguagePreference = tempTnccsLanguagePreference;
            } catch (TncException | UnsupportedOperationException e) {

                LOGGER.warn("Language preference could not be set"
                        + " and will be ignored.");
            }
            this.tempTnccsLanguagePreference = null;
        }
        return new ArrayList<>();
    }

}
