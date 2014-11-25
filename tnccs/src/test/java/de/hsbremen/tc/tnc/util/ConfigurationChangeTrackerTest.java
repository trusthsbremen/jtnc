package de.hsbremen.tc.tnc.util;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.imhandler.loader.DefaultFileChangeMonitor;
import de.hsbremen.tc.tnc.imhandler.loader.FileChangeListener;

public class ConfigurationChangeTrackerTest {

	private DefaultFileChangeMonitor monitor;
	private FileChangeListener observer;
	
	@Before
	public void setUp(){
		BasicConfigurator.configure();
		this.monitor = new DefaultFileChangeMonitor(new File("src/main/resources/tnc_config"), 1000, true);
		this.observer = new FileChangeListener() {
			
			@Override
			public void notifyDelete(File config) {
				System.err.println("File was deleted, or does not exist.");
				
			}
			
			@Override
			public void notifyChange(File config) {
				System.err.println("File was changed.");
				
			}
		};
		this.monitor.add(this.observer);
	}
	
	@Test
	public void test(){
		Thread t = new Thread(monitor);
		t.start();
		
		long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < 5000){
			
		}
		
		t.interrupt();
		System.out.println("Interrupted the Monitor");
	}
	
}
