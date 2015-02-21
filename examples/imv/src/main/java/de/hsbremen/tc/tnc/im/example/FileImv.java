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
package de.hsbremen.tc.tnc.im.example;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.file.FileImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;

/**
 * Custom file IMV.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class FileImv extends ImvAdapterIetf {

    /**
     * Creates the file IMV.
     */
    public FileImv() {
        super(new ImParameter(), new TncsAdapterFactoryIetf(),
                new DefaultImvSessionFactory(),
                new DefaultImSessionManager<IMVConnection, ImvSession>(3000),
                new FileImvEvaluatorFactory("/validation/file_imv.properties"),
                // testing needed because the component is of type testing
                new ImvConnectionAdapterFactoryIetf(
                        PaWriterFactory.createTestingDefault()),
                        PaReaderFactory.createTestingDefault());
    }
}
