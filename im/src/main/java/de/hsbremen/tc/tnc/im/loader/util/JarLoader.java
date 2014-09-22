package de.hsbremen.tc.tnc.im.loader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JarLoader {

	private static final String URL_SHEMA = "file://";
	
	public static List<String> readLines(File configFile,String lineClassifier) {
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
	
	public static <T> List<T> loadImList(List<String> lines) {
		List<T> imList = new ArrayList<>();
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
			T im = loadIm(name, mainClass, path);
			
			if(im != null){
				imList.add(im);
			}
		}
		
		return imList;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	private static <T> T loadIm(String name, String mainClass, String path){
		T im = null;
		
		URL url = null;
		try {
			url = new URL(URL_SHEMA + path);
		} catch (MalformedURLException e) {
			// TODO LOG and return null;
			e.printStackTrace();
			return im;
		}
		
		if(url != null){
			URLClassLoader urlLoader = new URLClassLoader(new URL[]{url});
			Class clazz = null;
			Constructor ctor = null;
			try {
				clazz = urlLoader.loadClass(mainClass);
				ctor = clazz.getConstructor();
				im = (T)ctor.newInstance();
			} catch (ClassNotFoundException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO LOG and return null;
				im = null;
				e.printStackTrace();
			}
			
			try {
				urlLoader.close();
			} catch (IOException e) {
				// TODO Log and do nothing.
				e.printStackTrace();
			}
		}
		
		return im;
	}
	
}
