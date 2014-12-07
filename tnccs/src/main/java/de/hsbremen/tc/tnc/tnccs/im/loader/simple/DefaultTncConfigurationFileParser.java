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
import de.hsbremen.tc.tnc.tnccs.im.loader.ImConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.TncConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;

public class DefaultTncConfigurationFileParser implements TncConfigurationFileParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTncConfigurationFileParser.class);
	private static final String URL_SHEMA = "file://";
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.imhandler.loader.TncConfigurationFileFilter#parseImConfigEntries(java.io.File)
	 */
	@Override
	public Set<ConfigurationEntry> parseConfigurationEntries(File configFile, ConfigurationLineClassifier classifier){
		
		Set<ConfigurationEntry> entries = new HashSet<>();
		
		if(classifier.linePrefix().equals(DefaultConfigurationLineClassifierEnum.JAVA_IMC.linePrefix()) || classifier.linePrefix().equals(DefaultConfigurationLineClassifierEnum.JAVA_IMV.linePrefix())){
			List<String> lines = this.getLines(configFile, classifier.linePrefix());
			entries.addAll(this.convertLinesToImConfigurationEntries(lines));
		}
		
		return entries;

	}
	
	
	private List<String> getLines(File configFile, String linePrefix) {
		List<String> lines = new ArrayList<>();
		// Construct BufferedReader from FileReader
		try{
			BufferedReader br = new BufferedReader(new FileReader(configFile));
		 
			String line = null;
			while ((line = br.readLine()) != null) {
				// only get lines specifying JAVA imc/s components  
				line = line.trim();
				if(line.startsWith(linePrefix)){
					lines.add(line);
				}
			}
		 
			br.close();
		}catch(IOException e){
			LOGGER.error("An error occured while trying to read the configuration file.\nConfiguration lines could not be loaded.\n", e);
			// return empty line list;
			lines = new ArrayList<>();
		}
		
		return lines;
	}
	
	private Set<ConfigurationEntry> convertLinesToImConfigurationEntries(List<String> lines) {
		Set<ConfigurationEntry> imSet = new HashSet<>();
		// if nothing to parse, return 
		if(lines == null || lines.size() <= 0){
			return imSet;
		}
		//TODO maybe a more complex pattern here
		String javaNaming = "([a-zA-Z_$]{1}[a-zA-Z_$0-9]*(\\.[a-zA-Z_$]{1}[a-zA-Z_$0-9]*)*)";
		String filePath = "((?!.*//.*)(?!.*/ .*)/{1}([^\\\\(){}:\\*\\?<>\\|\\\"\\'])+\\.(jar))";
		String imName = "\"([^\"]+)\"";
		//Pattern p = Pattern.compile("(JAVA-IMC|JAVA-IMV) \"([^\"]+)\" " + javaNaming + " " + filePath);
		Pattern p = Pattern.compile(imName + " " + javaNaming + " " + filePath);
		for (String line : lines) {
			Matcher m = p.matcher(line);
			m.find();
			String name = m.group(1).trim();
			String mainClass = m.group(2).trim();
			// 4 is an intermediate match
			String path = m.group(4).trim();
			try{
				URL url = new URL(URL_SHEMA + path);
				ConfigurationEntry cfg = new ImConfigurationEntry(name, mainClass, url);
				imSet.add(cfg);
			}catch(MalformedURLException e){
				LOGGER.error("MalformedURLException was thrown while creating configuration entry for IMC/V named " + name +".\nThe IMC/V will be ignored.\n", e);
			}
			
		}
		
		return imSet;
	}
}
