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
 * @author Carl-Heinz Genzel
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

            if (manager.hasRecommendation()) {

                super.getConnection().provideRecommendation(
                        manager.getRecommendation(this));
            }
        }
    }

}
