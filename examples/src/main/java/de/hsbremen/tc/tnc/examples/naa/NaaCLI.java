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
package de.hsbremen.tc.tnc.examples.naa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterIetfFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;

/**
 * The CLI to interact with the NAA.
 * 
 * @author Carl-Heinz Genzel
 * 
 */
public abstract class NaaCLI {

    
    /**
     * Main method to run the NAA.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // LOGGER
        BasicConfigurator.configure();

        Naa naa = new Naa();
        List<IMV> imvs = new ArrayList<>();
        imvs.add(new TestImvOs());
        naa.loadImv(imvs);

        String input = "";
        Scanner in = new Scanner(System.in);
        
        System.out.println("Usage: \n start //start server\n"
                + "stop //stop server\n"
                + "quit //close server (use always stop first)");
        try {

            while (!(input = in.nextLine()).contains("quit")) {
                System.out.print("#>");
                if (input.contains("start")) {
                    naa.start();
                }

                if (input.contains("stop")) {
                    naa.stop();
                }
            }

        } finally {
            in.close();
        }

    }

    /**
     * Overwritten default IMV to add custom elements. In this case the example
     * IMV to evaluate operating system attributes.
     *
     * @author Carl-Heinz Genzel
     * 
     */
    private static class TestImvOs extends ImvAdapterIetf {

        public TestImvOs() {
            super(
                    new ImParameter(),
                    new TncsAdapterIetfFactory(),
                    new DefaultImvSessionFactory(),
                    new DefaultImSessionManager<IMVConnection, ImvSession>(3000),
                    new OsImvEvaluatorFactory("/os_imv.properties"),
                    new ImvConnectionAdapterFactoryIetf(
                            PaWriterFactory.createProductionDefault()),
                    PaReaderFactory.createProductionDefault());
        }

    }
}
