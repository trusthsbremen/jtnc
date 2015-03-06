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
package de.hsbremen.tc.tnc.im.evaluate.example.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.TnccsAdapter;
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
 * component to watch changes on a file.
 *
 *
 */
public class FileImvEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {

    private static final Set<SupportedMessageType> SUPPORTED_MESSAGE_TYPES =
            new HashSet<>(
            Arrays.asList(new SupportedMessageType[] {
                    SupportedMessageTypeFactory
                    .createSupportedMessageType(
                            FileImvEvaluationUnit.VENDOR_ID,
                            FileImvEvaluationUnit.TYPE) }));

    private String evaluationValuesPath;

    /**
     * Creates the example evaluator factory with the given path to
     * a file containing reference values for the evaluation unit.
     *
     * @param evaluationValuesPath the reference value file
     */
    public FileImvEvaluatorFactory(final String evaluationValuesPath) {
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
        units.add(new FileImvEvaluationUnit(this.evaluationValuesPath,
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
