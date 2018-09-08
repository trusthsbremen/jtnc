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
package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;

/**
 * Default IMV session managing an IMV connection.
 *
 *
 */
public class DefaultImvSession extends
        AbstractDefaultImSession<ImvConnectionAdapter> implements ImvSession {

    /**
     * Creates an IMV session for the given connection.
     *
     * @param connection the connection for the session
     * @param connectionState the initial connection state
     * @param evaluators the evaluation composition for the session
     */
    DefaultImvSession(final ImvConnectionAdapter connection,
            final TncConnectionState connectionState,
            final ImvEvaluatorManager evaluators) {
        super(connection, connectionState, evaluators);
    }

    @Override
    public void triggerMessage(final ImMessageTriggerEnum reason)
            throws TncException {
        super.triggerMessage(reason);
        this.lookForRecommendation();
    }

    @Override
    public void handleMessage(final ImObjectComponent component)
            throws TncException {
        super.handleMessage(component);
        this.lookForRecommendation();
    }

    @Override
    public void solicitRecommendation() throws TncException {

        ImvRecommendationPair recommendation = null;

        if (super.getEvaluator() instanceof ImvEvaluatorManager) {
            recommendation = ((ImvEvaluatorManager) super.getEvaluator())
                    .getRecommendation(this);
        }

        super.getConnection().provideRecommendation(
                (recommendation != null) ? recommendation
                        : ImvRecommendationPairFactory
                        .getDefaultRecommendationPair());
    }

    /**
     * Checks if a recommendation for an IMV is ready to be collected.
     * If the a recommendation is ready, it will be collected and
     * send to the lower layers.
     *
     * @throws TncException if recommendation was ready but could
     * not be collected or send.
     */
    private void lookForRecommendation() throws TncException {

        if (super.getEvaluator() instanceof ImvEvaluatorManager) {

            ImvEvaluatorManager manager = (ImvEvaluatorManager) super
                    .getEvaluator();

            if (manager.hasRecommendation(this)) {

                super.getConnection().provideRecommendation(
                        manager.getRecommendation(this));
            }
        }
    }

}
