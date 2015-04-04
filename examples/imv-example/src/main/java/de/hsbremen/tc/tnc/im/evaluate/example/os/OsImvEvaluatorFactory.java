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
package de.hsbremen.tc.tnc.im.evaluate.example.os;

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
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImvEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Example IMV evaluator factory to compose a integrity measurement
 * for a operating system component.
 *
 *
 */
public class OsImvEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {

    private static final Set<SupportedMessageType> SUPPORTED_MESSAGE_TYPES =
            new HashSet<>(
            Arrays.asList(new SupportedMessageType[] {
                    SupportedMessageTypeFactory
                    .createSupportedMessageType(OsImvEvaluationUnit.VENDOR_ID,
                            OsImvEvaluationUnit.TYPE) }));

    private final String evaluationValuesPath;

    /**
     * Creates the example evaluator factory with the given path to
     * a file containing reference values for the evaluation unit.
     *
     * @param evaluationValuesPath the reference value file
     */
    public OsImvEvaluatorFactory(final String evaluationValuesPath) {
        this.evaluationValuesPath = evaluationValuesPath != null
                ? evaluationValuesPath : "";
    }

    @Override
    protected ImvEvaluatorManager createEvaluatorManager(
            final TnccsAdapter tncs,
            final ImParameter imParams) {

        NotNull.check("TNCS adapter cannot be null.", tncs);
        NotNull.check("Parameter cannot be null.", imParams);

        List<ImvEvaluationUnit> units = new ArrayList<>();
        units.add(new OsImvEvaluationUnit(this.evaluationValuesPath,
                tncs.getHandshakeRetryListener()));

        ImvEvaluator evaluator = new DefaultImvEvaluator(
                imParams.getPrimaryId(), units,
                new DefaultImValueExceptionHandler());

        Map<Long, ImvEvaluator> evaluators = new HashMap<>();
        evaluators.put(evaluator.getId(), evaluator);

        return new DefaultImvEvaluatorManager(
                SUPPORTED_MESSAGE_TYPES, evaluators);
    }

}
