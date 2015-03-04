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
package de.hsbremen.tc.tnc.examples.nar;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;

/**
 * Overwritten default IMC to add custom elements. In this case the example
 * IMC to evaluate operating system attributes.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class TestImcOs extends ImcAdapterIetf {

    /**
     * Creates the Test IMC for the operating system.
     */
    public TestImcOs() {
        super(
                new ImParameter(),
                new TnccAdapterFactoryIetf(),
                new DefaultImcSessionFactory(),
                new DefaultImSessionManager<IMCConnection, ImcSession>(3000),
                new OsImcEvaluatorFactory(),
                new ImcConnectionAdapterFactoryIetf(
                        PaWriterFactory.createProductionDefault()),
                PaReaderFactory.createProductionDefault());
    }

}