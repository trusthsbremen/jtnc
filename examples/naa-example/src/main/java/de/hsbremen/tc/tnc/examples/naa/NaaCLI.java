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
package de.hsbremen.tc.tnc.examples.naa;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

/**
 * The CLI to interact with the NAA.
 *
 *
 */
public abstract class NaaCLI {

    private static final Pattern CONFIG_FILE_PATH =
            Pattern.compile("(start) (([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+)");

    /**
     * Main method to run the NAA.
     *
     * @param args the main arguments
     */
    public static void main(final String[] args) {

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

                        File file = new File(m.group(2).trim());
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
