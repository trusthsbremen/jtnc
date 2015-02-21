package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;

public class DefaultConfigurationFileParserImJava implements
        ConfigurationFileParser {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultConfigurationFileParserImJava.class);
    private static final String URL_SHEMA = "file://";

    @Override
    public Set<ConfigurationEntry> parseConfigurationEntries(File configFile,
            ConfigurationLineClassifier classifier) {

        Set<ConfigurationEntry> entries = new HashSet<>();

        if (classifier.equals(DefaultConfigurationLineClassifierEnum.JAVA_IMC)
                || classifier.equals(
                        DefaultConfigurationLineClassifierEnum.JAVA_IMV)) {

            List<String> lines = this.getLines(configFile,
                    classifier.linePrefix());
            entries.addAll(this.convertLinesToImConfigurationEntries(
                    classifier.linePrefix(), lines));
        }

        return entries;

    }

    private List<String> getLines(File configFile, String linePrefix) {
        List<String> lines = new ArrayList<>();
        // Construct BufferedReader from FileReader
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            String line = null;
            while ((line = br.readLine()) != null) {
                // only get lines specifying JAVA imc/s components
                line = line.trim();
                if (line.startsWith(linePrefix)) {
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

    private Set<ConfigurationEntry> convertLinesToImConfigurationEntries(
            String linePrefix, List<String> lines) {
        Set<ConfigurationEntry> imSet = new HashSet<>();
        // if nothing to parse, return
        if (lines == null || lines.size() <= 0) {
            return imSet;
        }
        // TODO maybe a more complex pattern here
        final String javaNaming = "([a-zA-Z_$]{1}[a-zA-Z_$0-9]*"
                + "(\\.[a-zA-Z_$]{1}[a-zA-Z_$0-9]*)*)";
        final String filePath = "((?!.*//.*)(?!.*/ .*)/{1}"
                + "([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+\\.(jar))";
        final String imName = "\"([^\"]+)\"";
        Pattern p = Pattern.compile("^" + linePrefix + " " + imName + " "
                + javaNaming + " " + filePath + "$");

        final int nameGroupIdx = 1;
        final int classGroupIdx = 2;
        final int pathGroupIdx = 4;

        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                String name = m.group(nameGroupIdx).trim();
                String mainClass = m.group(classGroupIdx).trim();
                // 3 is an intermediate match
                String path = m.group(pathGroupIdx).trim();
                try {
                    URL url = new URL(URL_SHEMA + path);
                    ConfigurationEntry cfg = new DefaultConfigurationEntryImJava(
                            name, mainClass, url);
                    imSet.add(cfg);
                } catch (MalformedURLException e) {
                    LOGGER.error(
                            "MalformedURLException was thrown"
                            + " while creating configuration entry"
                            + " for IM(C/V )named "
                            + name + ". The IM (C/V) will be ignored.",
                            e);
                }
            } else {
                LOGGER.warn("Line " + line + " does not fit into the pattern"
                        + " for an IM(C/V) configuration line.");
            }

        }

        return imSet;
    }
}
