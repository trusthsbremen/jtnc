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

	public static ConfigurationFileChangeHandler getFileChangeListener(final NotifyTestObject o){
		return new ConfigurationFileChangeHandler() {
			
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
	public static String getConfigLineToDummyImc(){
	    String s = "JAVA-IMC \"Example IMC\" de.hsbremen.tc.tnc.DummyImc /home/sidanetdev/git/jtnc/tnccs/src/test/resources/DummyImc.jar";
	    return s;
	}
	
	public static String getConfigLineComment(){
		int i = new Random().nextInt(100);
		String s = "# Line comment " + i;
		return s;	
	}

	public static ConfigurationEntryChangeListener getConfigurationChangeListener(final NotifyTestObject o) {
		
	    
		return new ConfigurationEntryChangeListener() {
			
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
			public void notifyChange(Set<ConfigurationEntry> entries,
					ConfigurationLineClassifier classfier) {
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
	
