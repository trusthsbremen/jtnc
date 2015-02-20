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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;

/**
 * The CLI to interact with the NAR.
 * 
 * @author Carl-Heinz Genzel
 * 
 */
public class NarCLI {

    /**
     * Main method to run the NAA.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // LOGGER
        BasicConfigurator.configure();

        Nar nar = new Nar();
        List<IMC> imcs = new ArrayList<>();
        imcs.add(new TestImcOs());
        nar.loadImc(imcs);

        String input = "";
        Scanner in = new Scanner(System.in);
        
        System.out.println("Usage: \n handshake //start handshake\n"
                + "stop //stop handshake\n"
                + "quit //close client (use always stop first)");
        try {
            while (!(input = in.nextLine()).contains("quit")) {
                System.out.print("#>");
                if (input.contains("handshake")) {
                    try {
                        nar.startHandshake();
                    } catch (IOException e) {
                        System.err.println("Handshake could not be started: "
                                + e.getMessage());
                    }
                }

                if (input.contains("stop")) {
                    nar.stopHandshake();
                }
            }
        } catch (IOException e) {
            System.err.println("Handshake could not be stopped: "
                    + e.getMessage());
        } finally {
            in.close();
            nar.stop();
        }

    }

    /**
     * Overwritten default IMC to add custom elements. In this case the example
     * IMC to evaluate operating system attributes.
     *
     * @author Carl-Heinz Genzel
     * 
     */
    private static class TestImcOs extends ImcAdapterIetf {

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
}
