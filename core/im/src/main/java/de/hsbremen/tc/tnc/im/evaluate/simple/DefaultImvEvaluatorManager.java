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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.LeastPrivilegeRecommendationComparator;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Default IMV evaluator manager.
 *
 *
 */
public class DefaultImvEvaluatorManager implements ImvEvaluatorManager {

    private Map<Long, ImvEvaluator> evaluators;
    private Set<SupportedMessageType> supportedMessageTypes;
    private Map<Long, ImvRecommendationPair> evaluatorRecommendations;

    /**
     * Creates a default evaluator manager with the given evaluators.
     * @param evaluators the list of evaluators to be managed
     */
    public DefaultImvEvaluatorManager(
            final Map<Long, ImvEvaluator> evaluators) {
        this(new HashSet<SupportedMessageType>(), evaluators);
    }

    /**
     * Creates a default evaluator manager with the given evaluators.
     *
     * @param supportedMessageTypes the message types
     * supported by the evaluators
     * @param evaluators the list of evaluators to be managed
     */
    public DefaultImvEvaluatorManager(
            final Set<SupportedMessageType> supportedMessageTypes,
            final Map<Long, ImvEvaluator> evaluators) {
        this.evaluators = evaluators;
        this.supportedMessageTypes = supportedMessageTypes;
        this.evaluatorRecommendations = new HashMap<>(this.evaluators.size());
    }

    @Override
    public List<ImObjectComponent> evaluate(final ImSessionContext context) {
        List<ImObjectComponent> cmpList = new LinkedList<>();
        for (ImvEvaluator evaluator : this.evaluators.values()) {

            List<ImObjectComponent> components = null;
            components = evaluator.evaluate(context);
            if (components != null && components.size() > 0) {
                cmpList.addAll(components);
            }

            if (evaluator.hasRecommendation()) {
                this.evaluatorRecommendations.put(new Long(evaluator.getId()),
                        evaluator.getRecommendation(context));
            }

        }

        return cmpList;
    }

    @Override
    public List<ImObjectComponent> handle(
            final List<? extends ImObjectComponent> components,
            final ImSessionContext context) {

        List<ImObjectComponent> cmpList = new LinkedList<>();

        for (ImObjectComponent component : components) {
            // only use excl if there is an effective IM(C/V) ID
            if (component.getImFlags().contains(PaComponentFlagsEnum.EXCL)
                    && component.getCollectorId()
                        != HSBConstants.HSB_IM_ID_UNKNOWN) {

                if (this.evaluators.containsKey(component.getCollectorId())) {

                    ImvEvaluator evaluator = this.evaluators.get(component
                            .getCollectorId());

                    List<ImObjectComponent> parameterList =
                            new ArrayList<ImObjectComponent>();
                    parameterList.add(component);

                    List<ImObjectComponent> tmpComponents = evaluator.handle(
                            parameterList, context);

                    if (tmpComponents != null && tmpComponents.size() > 0) {
                        cmpList.addAll(tmpComponents);
                    }

                    if (evaluator.hasRecommendation()) {
                        this.evaluatorRecommendations.put(
                                new Long(evaluator.getId()),
                                evaluator.getRecommendation(context));
                    }
                }
            } else {
                for (ImvEvaluator evaluator : this.evaluators.values()) {

                    List<ImObjectComponent> parameterList =
                            new ArrayList<ImObjectComponent>();
                    parameterList.add(component);

                    List<ImObjectComponent> tmpComponents = evaluator.handle(
                            parameterList, context);

                    if (tmpComponents != null && tmpComponents.size() > 0) {
                        cmpList.addAll(tmpComponents);
                    }

                    if (evaluator.hasRecommendation()) {
                        this.evaluatorRecommendations.put(
                                new Long(evaluator.getId()),
                                evaluator.getRecommendation(context));
                    }
                }
            }
        }

        return cmpList;
    }

    @Override
    public List<ImObjectComponent> lastCall(final ImSessionContext context) {
        List<ImObjectComponent> cmpList = new LinkedList<>();

        for (ImvEvaluator evaluator : this.evaluators.values()) {
            List<ImObjectComponent> components = null;
            components = evaluator.lastCall(context);

            if (components != null && components.size() > 0) {
                cmpList.addAll(components);
            }

            if (evaluator.hasRecommendation()) {
                this.evaluatorRecommendations.put(new Long(evaluator.getId()),
                        evaluator.getRecommendation(context));
            }
        }

        return cmpList;
    }

    @Override
    public void terminate() {
        for (ImvEvaluator evaluator : this.evaluators.values()) {
            evaluator.terminate();
        }
    }

    @Override
    public Set<SupportedMessageType> getSupportedMessageTypes() {
        return Collections.unmodifiableSet((this.supportedMessageTypes != null)
                ? this.supportedMessageTypes
                : new HashSet<SupportedMessageType>());
    }

    /**
     * Provides a recommendation by asking all units and using the simple
     * recommendation comparator to find the recommendation with the least
     * privilege. Especially important for inheritance.
     *
     * @param context the context, which holds connection specific values
     * @return the recommendation pair (recommendation, result)
     */
    protected ImvRecommendationPair provideRecommendation(
            final ImSessionContext context) {
        if (this.evaluatorRecommendations != null
                && !this.evaluatorRecommendations.isEmpty()) {

            List<ImvRecommendationPair> recommendations = new LinkedList<>(
                    this.evaluatorRecommendations.values());
            // clear the recommendations for new ones
            this.evaluatorRecommendations.clear();

            Collections.sort(recommendations,
                    LeastPrivilegeRecommendationComparator.getInstance());
            // because of the sort get last from list which should be the most
            // severe
            return recommendations.get((recommendations.size() - 1));
        } else {
            // Defaults to don't know.
            return ImvRecommendationPairFactory.getDefaultRecommendationPair();
        }
    }

    @Override
    public ImvRecommendationPair getRecommendation(
            final ImSessionContext context) {
        for (ImvEvaluator evaluator : this.evaluators.values()) {
            if (!this.evaluatorRecommendations.containsKey(evaluator.getId())) {
                if (evaluator.hasRecommendation()) {
                    this.evaluatorRecommendations.put(
                            new Long(evaluator.getId()),
                            evaluator.getRecommendation(context));
                } // else ignore the evaluators opinion
            }
        }

        return this.provideRecommendation(context);
    }

    @Override
    public boolean hasRecommendation() {
        return (this.evaluators.size() == this.evaluatorRecommendations.size());
    }
}
