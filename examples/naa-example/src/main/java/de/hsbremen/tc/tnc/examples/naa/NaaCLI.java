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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.trustedcomputinggroup.tnc.ifimv.IMV;

/**
 * The CLI to interact with the NAA.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class NaaCLI {

    private static final Pattern CONFIG_FILE_PATH =
            Pattern.compile("(([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+)");

    /**
     * Main method to run the NAA.
     *
     * @param args the main arguments
     */
    public static void main(final String[] args) {
        // LOGGER
        BasicConfigurator.configure();

        Naa naa = new Naa();


        String input = "";
        Scanner in = new Scanner(System.in);

        String help = new StringBuffer()
        .append("\n=========================================")
        .append("\n| Network Access Authority Demonstrator |")
        .append("\n=========================================\n")
        .append("\nUsage:")
        .append("\n-------")
        .append("\nhelp \t\t\t show command help")
        .append("\n")
        .append("\nstart [file path] \t start NAR")
        .append("\nstop \t\t\t stop NAR")
        .append("\nquit \t\t\t quit input")
        .append("\n-------\n").toString();

        System.out.println(help);

        do {
            if (input.contains("start")) {
                if (input.length() > "start".length()) {
                    Matcher m  = CONFIG_FILE_PATH.matcher(input);
                    if (m.find()) {
                        File file = new File(m.group(1));
                        if (file.exists() && file.canRead()) {
                            naa.loadImvFromConfigurationFile(file);
                        } else {
                            System.err.println("File at "
                                    + file.getPath().toString()
                                    + " is not accessible."
                                    + " Instantiate IMV from code.");

                            List<IMV> imvs = new ArrayList<>();
                            imvs.add(new TestImvOs());
                            naa.loadImv(imvs);
                        }
                    } else {
                        System.err.println("No file specified."
                                + " Instantiate IMV from code.");
                        List<IMV> imvs = new ArrayList<>();
                        imvs.add(new TestImvOs());
                        naa.loadImv(imvs);
                    }
                } else {
                    System.err.println("No file specified."
                            + " Instantiate IMV from code.");
                    List<IMV> imvs = new ArrayList<>();
                    imvs.add(new TestImvOs());
                    naa.loadImv(imvs);
                }
                naa.start();
                System.out.println("");
            } else if (input.contains("stop")) {
                naa.stop();
                System.out.println("");
            } else if (input.contains("help")) {
                System.out.println(help);
                System.out.println("");
            }
            System.out.print("#>");
        } while (!(input = in.nextLine()).contains("quit"));

        in.close();
    }
}
