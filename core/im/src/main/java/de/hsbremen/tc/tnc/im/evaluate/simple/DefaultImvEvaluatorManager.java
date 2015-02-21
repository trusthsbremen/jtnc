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
 * @author Carl-Heinz Genzel
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
