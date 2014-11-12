package de.hsbremen.tc.tnc.im.evaluate.example.os.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationParameterParser {
	
	public static Properties loadProperties() throws IOException{
		
		Properties p = new Properties();
		
		
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("os_imv.properties"));
		
		p.load(stream);
		
		stream.close();
		
		return p;
	}
	
}
