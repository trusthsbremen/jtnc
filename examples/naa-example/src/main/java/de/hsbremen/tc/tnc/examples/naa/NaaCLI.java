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
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CLI to interact with the NAA.
 *
 *
 */
public abstract class NaaCLI {

    private static final Pattern CONFIG_FILE_PATH =
            Pattern.compile("(start) (((((\\w+\\:\\\\)|(\\\\))" +
                    "([^\\\\\\/(){}:*?<>|\"']+\\\\)*)|(((\\/)|(\\w+\\:\\/))" +
                    "([^\\\\\\/(){}:*?<>|\"']+\\/)*))([^\\\\\\/(){}:*?<>|\"']+))");

    /**
     * Main method to run the NAA.
     *
     * @param args the main arguments
     */
    public static void main(final String[] args) {
        
        String help = new StringBuffer()
        .append("\n=========================================")
        .append("\n| Network Access Authority Demonstrator |")
        .append("\n=========================================\n")
        .append("\n1) Simple NAA listening to handshake requests from NAR")
        .append("\n2) Active NAA initiating a handshake request with NAR")
        .append("\nSelect example to load:").toString();
        
        System.out.print(help);
        
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().trim();
        
        if (input.equals("1")){
            NaaCLI.getNaaCli(in);
        } else if (input.equals("2")){
            NaaCLI.getActiveNaaCli(in);
        } else {
            System.err.println("Could not recognize option. Cannot start.");
        }

    }
    
    public static void getNaaCli(Scanner in){
        
        String input = "";
        
        Naa naa = new Naa("/naa.properties");

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
                            naa.start();
                        } else {
                            System.err.println("File at "
                                    + file.getPath().toString()
                                    + " is not accessible. Cannot start.");
                        }
                    } else {
                        System.err.println("Invalid path. Cannot start.");
                    }
                } else {
                    System.err.println("No file specified. Cannot start.");
                }
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
    
    public static void getActiveNaaCli(Scanner in){
        String input = "";
        
        Naa naa = new Naa("/naa.properties");

        String help = new StringBuffer()
        .append("\n=========================================")
        .append("\n| Network Access Authority Demonstrator |")
        .append("\n=========================================\n")
        .append("\nUsage:")
        .append("\n-------")
        .append("\nhelp \t\t\t show command help")
        .append("\n")
        .append("\nstart [file path] \t start NAA")
        .append("\nstop \t\t\t stop NAA")
        .append("\nquit \t\t\t quit input")
        .append("\nhandshake \t\t ")
        .append("dialog to select a TNCC for handshake")
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
                            naa.start();
                        } else {
                            System.err.println("File at "
                                    + file.getPath().toString()
                                    + " is not accessible. Cannot start.");
                        }
                    } else {
                        System.err.println("Invalid path. Cannot start.");
                    }
                } else {
                    System.err.println("No file specified. Cannot start.");
                }
                System.out.println("");
                
            } else if (input.trim().equals("handshake")) {
                Collection<String> connectionIds = naa.getActiveConnectionById().keySet();
                if (connectionIds.isEmpty()){
                    System.out.println("No connections available for handshake.");
                } else {
                    System.out.println("Connections available for handshake:");
                    for (String id : connectionIds) {
                       System.out.println("- " + id);
                    }
                    System.out.print("\nSelect TNCC to load:");
                    input = in.nextLine();
                    String chosen = input.trim();
                    if ( connectionIds.contains(chosen)) {
                        System.out.print("Beginn handshake with " + chosen + ".");
                        naa.startHandshake(chosen);
                    } else {
                        System.out.println("Cannot start handshake. Invalid ID: " + chosen);
                    }
                }

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
