package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.AbstractDummy;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;

public class Dummy extends AbstractDummy {

	public static ConfigurationFileChangeListener getFileChangeListener(final NotifyTestObject o){
		return new ConfigurationFileChangeListener() {
			
			@Override
			public void notifyDelete() {
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
	
	public static String getConfigLineWin(){
        int i = new Random().nextInt(100);
        String s = "JAVA-IMC \"Example IMC"+i+"\" de.hsbremen.sidanet.nar.imc.ExampleImc"+i+" c:/home/tnc/example_imc"+i+".jar";
        return s;
    }
	
	public static String getConfigLineToDummyImc(){
	    String s = "JAVA-IMC \"Example IMC\" de.hsbremen.tc.tnc.DummyImc /home/sidanetdev/git/jtnc/core/tnccs/src/test/resources/DummyImc.jar";
	    return s;
	}
	
	public static String getConfigLineComment(){
		int i = new Random().nextInt(100);
		String s = "# Line comment " + i;
		return s;	
	}

	public static ConfigurationEntryHandler getConfigurationChangeListener(final NotifyTestObject o) {
		
	    
		return new ConfigurationEntryHandler() {
			
		    Set<ConfigurationLineClassifier> classifiers =
		            new HashSet<ConfigurationLineClassifier>(
		            Arrays.asList(DefaultConfigurationLineClassifierEnum.JAVA_IMC));
		    
            @Override
            public Set<ConfigurationLineClassifier> getSupportedConfigurationLines() {
                return classifiers;
            }

            @Override
			public void notifyDelete() {
				o.deleteNotified = true;
				
			}
			
			@Override
			public void notifyChange(ConfigurationLineClassifier classfier,
			        Set<ConfigurationEntry> entries) {
				o.changeNotified = true;
				System.out.println("Change notified for classifier: " + classfier.toString());
				System.out.println(Arrays.toString(entries.toArray()));
			}
		};

	}
	
	public static GlobalHandshakeRetryListener getRetryListener(){
        return new GlobalHandshakeRetryListener() {
            
            @Override
            public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
                    throws TncException {
                System.out.println("Retry requested with reason: "+ reason.toString());
                
            }
        };
    }
}
	
