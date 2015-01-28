package de.hsbremen.tc.tnc.im.evaluate.example.os.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationParameterParser {
	
	public static Properties loadProperties(String evaluationValuesFile) throws IOException{
		
		Properties p = new Properties();

		BufferedInputStream stream = new BufferedInputStream(evaluationValuesFile.getClass().getResourceAsStream(evaluationValuesFile));
		
		p.load(stream);
		
		stream.close();
		
		return p;
	}
	
}
