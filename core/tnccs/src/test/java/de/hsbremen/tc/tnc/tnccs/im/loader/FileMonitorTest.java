package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy.NotifyTestObject;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;

public class FileMonitorTest {

	private ConfigurationFileChangeMonitor monitor;
	private File file = new File("src/test/resources/tnc_config.test");
	private NotifyTestObject o;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp() throws IOException{
		this.monitor = new DefaultConfigurationFileChangeMonitor(file, 200, false);
		o = new Dummy.NotifyTestObject();
		this.monitor.add(Dummy.getFileChangeListener(o));
		this.file.createNewFile();
	}
	
	@Test
	public void testChange() throws IOException{
		
		this.monitor.start();
		
		
		FileWriter w = new FileWriter(this.file);
		w.append(Dummy.getConfigLine());
		w.flush();
		w.close();
		
		// make a short break because the file monitor has a intervall
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.monitor.stop();
		
		Assert.assertTrue(o.changeNotified);
		
	}
	
	@Test
	public void testDelete() throws IOException{
		this.monitor.start();
		
		this.file.delete();
		
		// make a short break because the file monitor has a interval
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.monitor.stop();

		Assert.assertTrue(o.deleteNotified);
		
	}
	
}
