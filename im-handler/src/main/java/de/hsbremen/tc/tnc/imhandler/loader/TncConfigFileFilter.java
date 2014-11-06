package de.hsbremen.tc.tnc.imhandler.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TncConfigFileFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TncConfigFileFilter.class);
	
	private static final String URL_SHEMA = "file://";
	
	static List<String> getLines(File configFile,String lineClassifier) {
		List<String> lines = new ArrayList<>();
		// Construct BufferedReader from FileReader
		try{
			BufferedReader br = new BufferedReader(new FileReader(configFile));
		 
			String line = null;
			while ((line = br.readLine()) != null) {
				// only get lines specifying JAVA imc/s components  
				if(line.startsWith(lineClassifier)){
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
	
	static List<ImLoadParameter> convertLinesToLoadingList(List<String> lines) {
		List<ImLoadParameter> imList = new ArrayList<>();
		// if nothing to parse, return 
		if(lines == null || lines.size() <= 0){
			return imList;
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
				ImLoadParameter param = new ImLoadParameter(name, mainClass, url);
				imList.add(param);
			}catch(MalformedURLException e){
				LOGGER.error("MalformedURLException was thrown while creating ImLoadingParameter for IMC/V named " + name +".\nThe IMC/V will be ignored.\n", e);
			}
			
		}
		
		return imList;
	}
}
