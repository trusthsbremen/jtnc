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
package de.hsbremen.tc.tnc.im.evaluate.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Default IMC evaluator manager.
 *
 *
 */
public class DefaultImcEvaluatorManager implements ImcEvaluatorManager {

    private Map<Long, ImcEvaluator> evaluators;
    private Set<SupportedMessageType> supportedMessageTypes;

    /**
     * Creates a default evaluator manager with the given evaluators.
     * @param evaluators the list of evaluators to be managed
     */
    public DefaultImcEvaluatorManager(
            final Map<Long, ImcEvaluator> evaluators) {
        this.evaluators = evaluators;
        this.supportedMessageTypes = new HashSet<>();
    }

    /**
     * Creates a default evaluator manager with the given evaluators.
     *
     * @param supportedMessageTypes the message types
     * supported by the evaluators
     * @param evaluators the list of evaluators to be managed
     */
    public DefaultImcEvaluatorManager(
            final Set<SupportedMessageType> supportedMessageTypes,
            final Map<Long, ImcEvaluator> evaluators) {
        this.evaluators = evaluators;
        this.supportedMessageTypes = supportedMessageTypes;
    }

    @Override
    public List<ImObjectComponent> evaluate(final ImSessionContext context) {
        List<ImObjectComponent> cmpList = new LinkedList<>();
        for (ImcEvaluator evaluator : this.evaluators.values()) {

            List<ImObjectComponent> components = null;
            components = evaluator.evaluate(context);

            if (components != null && components.size() > 0) {
                cmpList.addAll(components);
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

                    List<ImObjectComponent> parameterList =
                            new ArrayList<ImObjectComponent>();
                    parameterList.add(component);

                    List<ImObjectComponent> tmpComponents = this.evaluators
                            .get(component.getCollectorId())
                            .handle(parameterList, context);

                    if (tmpComponents != null && tmpComponents.size() > 0) {
                        cmpList.addAll(tmpComponents);
                    }
                }
            } else {
                for (ImcEvaluator evaluator : this.evaluators.values()) {

                    List<ImObjectComponent> parameterList =
                            new ArrayList<ImObjectComponent>();
                    parameterList.add(component);

                    List<ImObjectComponent> tmpComponents =
                            evaluator.handle(parameterList, context);

                    if (tmpComponents != null && tmpComponents.size() > 0) {
                        cmpList.addAll(tmpComponents);
                    }
                }
            }
        }

        return cmpList;
    }

    @Override
    public List<ImObjectComponent> lastCall(final ImSessionContext context) {
        List<ImObjectComponent> cmpList = new LinkedList<>();
        for (ImcEvaluator evaluator : this.evaluators.values()) {

            List<ImObjectComponent> components = null;
            components = evaluator.lastCall(context);

            if (components != null && components.size() > 0) {
                cmpList.addAll(components);
            }
        }

        return cmpList;
    }

    @Override
    public void terminate() {
        for (ImcEvaluator evaluator : this.evaluators.values()) {
            evaluator.terminate();
        }
    }

    @Override
    public Set<SupportedMessageType> getSupportedMessageTypes() {
        return Collections.unmodifiableSet((this.supportedMessageTypes != null)
                ? this.supportedMessageTypes
                : new HashSet<SupportedMessageType>());
    }

}
