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
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.example.file.FileImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.simple.DefaultImcEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

/**
 * Example IMC evaluator factory to compose a integrity measurement
 * component of several evaluation units.
 *
 * @author Carl-Heinz Genzel
 *
 */
@Deprecated
public class CombinedImcEvaluatorFactory extends
    AbstractImEvaluatorFactoryIetf {

    private static final Set<SupportedMessageType> SUPPORTED_MESSAGE_TYPES =
            new HashSet<>(
            Arrays.asList(new SupportedMessageType[] {
            // no other types needed because DefaultImcEvaluationUnit has
            // any/any.
            SupportedMessageTypeFactory.createSupportedMessageType(
                    DefaultImcEvaluationUnit.VENDOR_ID,
                    DefaultImcEvaluationUnit.TYPE) }));

    private final String filePath;
    private final String messageDigestIdentifier;


    /**
     * Creates the factory with the given algorithm identifier for a
     * hash algorithm and a path to the monitored file. The parameters are
     * needed for the file evaluation unit.
     *
     * @param messageDigestIdentifier the algorithm identifier (e.g. SHA-1)
     * @param filePath the path to the file
     */
    public CombinedImcEvaluatorFactory(final String messageDigestIdentifier,
            final String filePath) {
        this.filePath = filePath;
        this.messageDigestIdentifier = messageDigestIdentifier;
    }

    @Override
    protected ImcEvaluatorManager createEvaluatorManager(
            final TnccsAdapter tncc,
            final ImParameter imParams) {

        List<ImcEvaluationUnit> units = new ArrayList<>();
        units.add(new OsImcEvaluationUnit(tncc.getHandshakeRetryListener()));
        units.add(new FileImcEvaluationUnit(this.messageDigestIdentifier,
                this.filePath, tncc.getHandshakeRetryListener()));
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
