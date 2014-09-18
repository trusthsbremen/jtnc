package de.hsbremen.tc.tnc.im.loader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TncConfigFilter {
	
	public static List<String> getLines(File configFile,String lineClassifier) {
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
			// TODO LOG and return empty list;
			e.printStackTrace();
		}
		
		return lines;
	}
}
