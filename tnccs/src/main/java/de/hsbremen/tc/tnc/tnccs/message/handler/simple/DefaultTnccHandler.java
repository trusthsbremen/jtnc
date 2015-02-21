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
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultTnccHandler implements TnccHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultTnccHandler.class);

    private final Attributed sessionAttributes;
    private final String tnccLanguagePreference;

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
            } catch (TncException | UnsupportedOperationException e) {

                LOGGER.warn("Language preference could not be set"
                        + " and will be ignored.");
            }
            this.tempTnccsLanguagePreference = null;
        }
        return new ArrayList<>();
    }

}
