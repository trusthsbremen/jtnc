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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapterFactoryIetf;

/**
 * The CLI to interact with the NAA.
 *
 *
 */
public abstract class NaaCLI {

    private static final Pattern CONFIG_FILE_PATH =
            Pattern.compile("(((((\\w+\\:\\\\)|(\\\\))" +
                    "([^\\\\\\/(){}:*?<>|\"']+\\\\)*)|(((\\/)|(\\w+\\:\\/))" +
                    "([^\\\\\\/(){}:*?<>|\"']+\\/)*))([^\\\\\\/(){}:*?<>|\"']+))");

    private static final File PROPERTIES_FILE = Paths.get(
            System.getProperty("user.dir"), "config", "config.properties").toFile();
    private static final String PROP_NAME_TNC_CONFIG_LOCATION = "tnc_config_location";
    private static final String DEFAULT_TNC_CONFIG_LOCATION = Paths
            .get(System.getProperty("user.dir"), "config", "tnc_config")
            .toAbsolutePath().toString();

    public static Properties getDefaultProperties(){
        Properties properties = new Properties();
        properties.setProperty(Naa.PROP_NAME_FILE_CHECK_INTERVAL, Naa.DEFAULT_FILE_CHECK_INTERVAL);
        properties.setProperty(Naa.PROP_NAME_MAX_MSG_SIZE, Naa.DEFAULT_MAX_MSG_SIZE);
        properties.setProperty(Naa.PROP_NAME_MAX_ROUND_TRIP, Naa.DEFAULT_MAX_ROUND_TRIP);
        properties.setProperty(Naa.PROP_NAME_NAA_PORT, Naa.DEFAULT_NAA_PORT);
        properties.setProperty(Naa.PROP_NAME_SESSION_CLEAN_INTERVAL, Naa.DEFAULT_SESSION_CLEAN_INTERVAL);
        properties.setProperty(Naa.PROP_NAME_IM_DEFAULT_TIMEOUT,Long.toString(ImvAdapterFactoryIetf.DEFAULT_TIMEOUT));
        properties.setProperty(NaaCLI.PROP_NAME_TNC_CONFIG_LOCATION, NaaCLI.DEFAULT_TNC_CONFIG_LOCATION);
        
        return properties;
    }
    
    /**
     * Main method to run the NAA.
     *
     * @param args the main arguments
     */
    public static void main(final String[] args) {
        
        Properties properties = null;
        
        if (PROPERTIES_FILE.isFile() && PROPERTIES_FILE.canRead()) {
                try {
                    properties = new Properties();
                    properties.load(new FileInputStream(PROPERTIES_FILE));
                } catch (IOException e) {
                    System.err.println("Could not read properties, will use defaults: " + e.getMessage());
                    properties = NaaCLI.getDefaultProperties();
                }
        } else {
            properties = NaaCLI.getDefaultProperties();
        }
        
        Naa naa = new Naa(properties);
        
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
        .append("\nstart \t\t\t start NAA")
        .append("\nstop \t\t\t stop NAA")
        .append("\nquit \t\t\t quit input")
        .append("\n-------\n").toString();

        System.out.println(help);

        do {
            if (input.trim().startsWith("start")) {
                if (properties.containsKey(PROP_NAME_TNC_CONFIG_LOCATION)) {
                    Matcher m  = CONFIG_FILE_PATH.matcher(
                            (String)properties.getOrDefault(PROP_NAME_TNC_CONFIG_LOCATION,
                                    DEFAULT_TNC_CONFIG_LOCATION));
                    if (m.find()) {
                        
                        File file = new File(m.group(1).trim());
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
            } else if (input.trim().startsWith("stop")) {
                naa.stop();
                System.out.println("");
            } else if (input.trim().startsWith("help")) {
                System.out.println(help);
                System.out.println("");
            }
            System.out.print("#>");
        } while (!(input = in.nextLine()).contains("quit"));

        in.close();
    }
}
