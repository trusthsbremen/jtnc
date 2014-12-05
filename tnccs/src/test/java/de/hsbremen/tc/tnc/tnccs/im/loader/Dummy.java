package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.AbstractDummy;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.FileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;

public class Dummy extends AbstractDummy {

	public static FileChangeListener getFileChangeListener(final NotifyTestObject o){
		return new FileChangeListener() {
			
			@Override
			public void notifyDelete(File config) {
				o.deleteNotified = true;
				
			}
			
			@Override
			public void notifyChange(File config) {
				o.changeNotified = true;
				
			}
		};
	}
	
	public static class NotifyTestObject{
		
		public boolean deleteNotified;
		public boolean changeNotified;

	}
	
	public static String getConfigLine(){
		int i = new Random().nextInt(100);
		String s = "JAVA-IMC \"Example IMC"+i+"\" de.hsbremen.sidanet.nar.imc.ExampleImc"+i+" /home/tnc/example_imc"+i+".jar";
		return s;
	}
	
	public static String getConfigLineComment(){
		int i = new Random().nextInt(100);
		String s = "# Line comment " + i;
		return s;	
	}

	public static Map<ConfigurationChangeListener,Set<ConfigurationLineClassifier>> getConfigurationChangeListener(final NotifyTestObject o) {
		
		ConfigurationChangeListener listener = new ConfigurationChangeListener() {
			
			@Override
			public void notifyDelete() {
				o.deleteNotified = true;
				
			}
			
			@Override
			public void notifyChange(Set<ConfigurationEntry> entries,
					ConfigurationLineClassifier classfier) {
				o.changeNotified = true;
				System.out.println("Change notified for classifier: " + classfier.toString());
				System.out.println(Arrays.toString(entries.toArray()));
			}
		};
		
		Set<ConfigurationLineClassifier> classifiers = new HashSet<>(); 
		classifiers.add(DefaultConfigurationLineClassifierEnum.JAVA_IMC);
		
		Map<ConfigurationChangeListener, Set<ConfigurationLineClassifier>> map = new HashMap<>();
		map.put(listener, classifiers);
		
		return map;
		
	}
}
	
