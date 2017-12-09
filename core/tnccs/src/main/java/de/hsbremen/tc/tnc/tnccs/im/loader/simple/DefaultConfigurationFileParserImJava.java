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
package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums
.DefaultConfigurationLineClassifierEnum;

/**
 * Default parser to parse configuration lines into configuration entry objects.
 * Currently it only supports Java based IM(C/V) configuration lines. Other line
 * are ignored.
 *
 *
 */
public class DefaultConfigurationFileParserImJava implements
        ConfigurationFileParser {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultConfigurationFileParserImJava.class);
    private static final String URL_SHEMA = "file://";

    private final ConfigurationLineClassifier lineClassifier;

    /**
     * Creates the default parser to parse configuration lines either for IMC or
     * for IMV.
     *
     * @param server true if the parser should parse configuration lines for
     * IMV, false for IMC
     */
    public DefaultConfigurationFileParserImJava(final boolean server) {
        if (server) {
            this.lineClassifier =
                    DefaultConfigurationLineClassifierEnum.JAVA_IMV;
        } else {
            this.lineClassifier =
                    DefaultConfigurationLineClassifierEnum.JAVA_IMC;
        }
    }

    @Override
    public Set<ConfigurationLineClassifier> getSupportedConfigurationLines() {

        return new HashSet<>(Arrays.asList(this.lineClassifier));
    }

    @Override
    public Map<ConfigurationLineClassifier, Set<ConfigurationEntry>>
    parseConfigurationEntries(final File configFile) {

        Map<ConfigurationLineClassifier, Set<ConfigurationEntry>> entries =
                new HashMap<>();

        List<String> lines = this.getLines(configFile);
        entries.put(this.lineClassifier,
                this.convertLinesToImConfigurationEntries(lines));

        return entries;

    }

    /**
     * Filters the configuration file searching for parsable lines.
     *
     * @param configFile the file to parse
     * @return a list of parsable lines
     */
    private List<String> getLines(final File configFile) {
        List<String> lines = new ArrayList<>();
        // Construct BufferedReader from FileReader
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            String line = null;
            while ((line = br.readLine()) != null) {
                // only get lines specifying JAVA imc/s components
                line = line.trim();
                if (line.startsWith(this.lineClassifier.linePrefix())) {
                    lines.add(line);
                }
            }

            br.close();
        } catch (IOException e) {
            LOGGER.error(
                    "An error occured while trying to read the configuration "
                            + "file. Configuration lines could not be loaded.",
                    e);
            // return empty line list;
            lines = new ArrayList<>();
        }

        return lines;
    }

    /**
     * Converts a list of strings to configuration entries for IM(C/V) objects.
     *
     * @param lines the list of strings to convert
     * @return a set of configuration entries
     */
    private Set<ConfigurationEntry> convertLinesToImConfigurationEntries(
            final List<String> lines) {
        Set<ConfigurationEntry> imSet = new HashSet<>();
        // if nothing to parse, return
        if (lines == null || lines.size() <= 0) {
            return imSet;
        }
        // TODO maybe a more complex pattern here
        final String javaNaming = "([a-zA-Z_$]{1}[a-zA-Z_$0-9]*"
                + "(\\.[a-zA-Z_$]{1}[a-zA-Z_$0-9]*)*)";
        final String filePath = "(([\\w]:)?(?!.*//.*)(?!.*/ .*)/{1}"
                + "([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+\\.(jar))";
        final String imName = "\"([^\"]+)\"";
        Pattern p = Pattern.compile("^" + this.lineClassifier.linePrefix()
                + " " + imName + " " + javaNaming + " " + filePath + "$");
        final int nameGroupIdx = 1;
        final int classGroupIdx = 2;
        // 3 is an intermediate match
        final int pathGroupIdx = 4;

        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                String name = m.group(nameGroupIdx).trim();
                String mainClass = m.group(classGroupIdx).trim();
                String path = m.group(pathGroupIdx).trim();
                try {
                    URL url = new URL(URL_SHEMA + path);
                    ConfigurationEntry cfg =
                            new DefaultConfigurationEntryImJava(
                            name, mainClass, url);
                    imSet.add(cfg);

                } catch (MalformedURLException e) {
                    LOGGER.error("MalformedURLException was thrown"
                            + " while creating configuration entry"
                            + " for IM(C/V )named " + name
                            + ". The IM (C/V) will be ignored.", e);
                }
            } else {
                LOGGER.warn("Line " + line + " does not fit into the pattern"
                        + " for an IM(C/V) configuration line.");
            }

        }

        return imSet;
    }
}
