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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.LeastPrivilegeRecommendationComparator;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Generic handler to handle messages destined to/from a TNCS.
 *
 *
 */
public class DefaultTncsHandler implements TncsHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultTncsHandler.class);

    private final Attributed sessionAttributes;
    private final String tnccLanguagePreference;

    private String tempTnccsLanguagePrefernce;
    private TncConnectionState state;
    private boolean handshakeStartet;

    /**
     * Creates a TNCS message handler with the given
     * accessible session and/or connection attributes.
     *
     * @param sessionAttributes the session/connection attributes
     */
    public DefaultTncsHandler(final Attributed sessionAttributes) {
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
                        "TnccsMessage with language preference "
                        + "could not be created and will be ignored.",
                        e);
            }
            this.handshakeStartet = false;
        }

        return messages;

    }

    @Override
    public List<TnccsMessage> forwardMessage(final TnccsMessage message) {

        if (message == null || message.getValue() == null) {
            LOGGER.debug("Because Message or message value is null, "
                    + "it is ignored.");
            return new ArrayList<TnccsMessage>();
        }

        TnccsMessageValue value = message.getValue();

        LOGGER.debug("Message value received: " + value.toString());

        if (value instanceof PbMessageValueLanguagePreference) {
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

        if (value instanceof PbMessageValueLanguagePreference) {
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
            this.tempTnccsLanguagePrefernce = lang.trim();
        }
    }

    @Override
    public List<TnccsMessage> lastCall() {
        if (this.tempTnccsLanguagePrefernce != null) {
            try {
                this.sessionAttributes
                        .setAttribute(TncCommonAttributeTypeEnum
                                .TNC_ATTRIBUTEID_PREFERRED_LANGUAGE,
                                this.tempTnccsLanguagePrefernce);

            } catch (TncException | UnsupportedOperationException e) {
                LOGGER.warn("Language preference could not be set"
                        + " and will be ignored.");
            }
            this.tempTnccsLanguagePrefernce = null;
        }
        return new ArrayList<>();
    }

    @Override
    public List<TnccsMessage> provideRecommendation(
            final List<ImvRecommendationPair> imvResults) {
        List<TnccsMessage> messages = new ArrayList<>(0);
        List<ImvRecommendationPair> imvResultsCopy = imvResults;

        TncConnectionState state =
                DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE;

        if (imvResultsCopy == null || imvResultsCopy.isEmpty()) {
            imvResultsCopy = new ArrayList<>(Arrays.asList(
                    ImvRecommendationPairFactory
                    .getDefaultRecommendationPair()));
        }
        Collections.sort(imvResultsCopy,
                LeastPrivilegeRecommendationComparator.getInstance());
        // because of the sort get last from list which should be the most
        // severe //
        ImvRecommendationPair leastEqualWeightPrivilege = imvResultsCopy
                .get((imvResultsCopy.size() - 1));

        messages = this
                .mapRecommendationToMessages(leastEqualWeightPrivilege);
        state = this.mapRecommendationToState(leastEqualWeightPrivilege);

        this.setConnectionState(state);

        return messages;

    }

    /**
     * Creates messages containing the integrity check handsheck
     * result according to the given recommendation pair.
     *
     * @param recommendation the overall recommendation
     * @return a list of messages with the result
     */
    private List<TnccsMessage> mapRecommendationToMessages(
            final ImvRecommendationPair recommendation) {

        List<TnccsMessage> messages = new ArrayList<>(2);

        ImvActionRecommendationEnum action = recommendation.getRecommendation();
        ImvEvaluationResultEnum result = recommendation.getResult();

        PbMessageAccessRecommendationEnum pbAction =
                PbMessageAccessRecommendationEnum.DENIED;

        switch (action) {
        case TNC_IMV_ACTION_RECOMMENDATION_ALLOW:
            pbAction = PbMessageAccessRecommendationEnum.ALLOWED;
            break;
        case TNC_IMV_ACTION_RECOMMENDATION_ISOLATE:
            pbAction = PbMessageAccessRecommendationEnum.QURANTINED;
            break;
        default:
            // is already denied so do nothing
            break;
        }

        try {
            TnccsMessage message = PbMessageFactoryIetf
                    .createAccessRecommendation(pbAction);
            messages.add(message);
        } catch (ValidationException e) {
            LOGGER.error(
                    "Could not create access recommendation. "
                    + "This should never happen.",
                    e);
        }

        PbMessageAssessmentResultEnum pbResult =
                PbMessageAssessmentResultEnum.ERROR;

        switch (result) {
        case TNC_IMV_EVALUATION_RESULT_COMPLIANT:
            pbResult = PbMessageAssessmentResultEnum.COMPLIANT;
            break;
        case TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR:
            pbResult = PbMessageAssessmentResultEnum.SIGNIFICANT_DIFFERENCES;
            break;
        case TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR:
            pbResult = PbMessageAssessmentResultEnum.MINOR_DIFFERENCES;
            break;
        case TNC_IMV_EVALUATION_RESULT_DONT_KNOW:
            pbResult = PbMessageAssessmentResultEnum.INSUFFICIANT_ATTRIBUTES;
            break;
        default:
            // already an error so do nothing
            break;
        }

        try {
            TnccsMessage message = PbMessageFactoryIetf
                    .createAssessmentResult(pbResult);
            messages.add(message);
        } catch (ValidationException e) {
            LOGGER.error(
                    "Could not create assessment result. "
                    + "This should never happen.",
                    e);
        }

        if (LOGGER.isDebugEnabled()) {
            StringBuilder b = new StringBuilder();
            b.append("TNCS recommends ")
            .append(pbAction.toString())
            .append(" access for the result ")
            .append(pbResult.toString());

            try {
                Object connectionId = this.sessionAttributes.getAttribute(
                        TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_IFT_ID);
                if (connectionId instanceof String) {
                    b.append(" for the connection with ID ")
                    .append((String) connectionId);
                }
            } catch (TncException e) {
               LOGGER.debug("Could not identify connection "
                       + "via session attributes.");
            }
            b.append(".");
            LOGGER.debug(b.toString());
        }

        return messages;
    }

    /**
     * Maps a action recommendation to a connection state
     * reflecting the action.
     *
     * @param recommendation the overall recommendation
     * @return a connection state
     */
    private TncConnectionState mapRecommendationToState(
            final ImvRecommendationPair recommendation) {

        ImvActionRecommendationEnum action =
                recommendation.getRecommendation();
        TncConnectionState state =
                DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE;

        switch (action) {
        case TNC_IMV_ACTION_RECOMMENDATION_ALLOW:
            state = DefaultTncConnectionStateEnum
                .TNC_CONNECTION_STATE_ACCESS_ALLOWED;
            break;
        case TNC_IMV_ACTION_RECOMMENDATION_ISOLATE:
            state = DefaultTncConnectionStateEnum
                .TNC_CONNECTION_STATE_ACCESS_ISOLATED;
            break;
        default:
            // already none so do nothing
            break;
        }

        return state;
    }

}
