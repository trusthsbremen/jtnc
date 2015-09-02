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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccsAdapter;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default IMC evaluator factory to compose the integrity measurement
 * components.
 *
 *
 */
public class DefaultImcEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {

    private static final Set<SupportedMessageType> SUPPORTED_MESSAGE_TYPES =
            new HashSet<>(
            Arrays.asList(new SupportedMessageType[] {
                    SupportedMessageTypeFactory
                    .createSupportedMessageType(
                            DefaultImcEvaluationUnit.VENDOR_ID,
                            DefaultImcEvaluationUnit.TYPE) }));

    @Override
    protected ImcEvaluatorManager createEvaluatorManager(
            final TnccsAdapter tncc, final ImParameter imParams) {

        NotNull.check("TNCC adapter cannot be null.", tncc);
        NotNull.check("Parameter cannot be null.", imParams);

        List<ImcEvaluationUnit> units = new ArrayList<>();
        units.add(new DefaultImcEvaluationUnit(
                tncc.getHandshakeRetryListener()));

        ImcEvaluator evaluator = new DefaultImcEvaluator(
                imParams.getPrimaryId(), units,
                new DefaultImValueExceptionHandler());

        Map<Long, ImcEvaluator> evaluators = new HashMap<>();
        evaluators.put(evaluator.getId(), evaluator);

        return new DefaultImcEvaluatorManager(
                SUPPORTED_MESSAGE_TYPES, evaluators);
    }
}
