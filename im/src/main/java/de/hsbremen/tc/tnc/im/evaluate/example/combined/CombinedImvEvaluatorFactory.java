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
package de.hsbremen.tc.tnc.im.evaluate.example.combined;

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
import de.hsbremen.tc.tnc.im.evaluate.example.file.FileImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple
.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

/**
 * Example IMV evaluator factory to compose a integrity measurement component of
 * several evaluation units.
 *
 * @author Carl-Heinz Genzel
 *
 */
@Deprecated
public class CombinedImvEvaluatorFactory extends
    AbstractImEvaluatorFactoryIetf {

    private static final Set<SupportedMessageType> SUPPORTED_MESSAGE_TYPES =
            new HashSet<>(
            Arrays.asList(new SupportedMessageType[] {
            // no other types needed because DefaultImvEvaluationUnit has
            // any/any.
            SupportedMessageTypeFactory.createSupportedMessageType(
                    DefaultImvEvaluationUnit.VENDOR_ID,
                    DefaultImvEvaluationUnit.TYPE), }));

    private final String osEvaluationValuesPath;
    private final String fileEvaluationValuesPath;

    /**
     * Creates the example evaluator factory with the given path to files
     * containing reference values for the evaluation units.
     *
     * @param osEvaluationValuesPath the reference value file
     * @param fileEvaluationValuesPath the reference value file
     */
    public CombinedImvEvaluatorFactory(final String osEvaluationValuesPath,
            final String fileEvaluationValuesPath) {
        this.fileEvaluationValuesPath = fileEvaluationValuesPath;
        this.osEvaluationValuesPath = osEvaluationValuesPath;
    }

    @Override
    protected ImvEvaluatorManager createEvaluatorManager(
            final TnccsAdapter tncs,
            final ImParameter imParams) {

        List<ImvEvaluationUnit> units = new ArrayList<>();
        units.add(new OsImvEvaluationUnit(this.osEvaluationValuesPath,
                tncs.getHandshakeRetryListener()));
        units.add(new FileImvEvaluationUnit(this.fileEvaluationValuesPath,
                tncs.getHandshakeRetryListener()));
        units.add(new DefaultImvEvaluationUnit(
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
