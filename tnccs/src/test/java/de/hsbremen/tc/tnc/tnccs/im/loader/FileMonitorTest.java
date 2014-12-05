package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy;
import de.hsbremen.tc.tnc.tnccs.im.loader.FileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy.NotifyTestObject;

public class FileMonitorTest {

	private FileChangeMonitor monitor;
	private File file = new File("src/test/resources/tnc_config.test");
	private NotifyTestObject o;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp() throws IOException{
		this.monitor = new DefaultFileChangeMonitor(file);
		o = new Dummy.NotifyTestObject();
		this.monitor.add(Dummy.getFileChangeListener(o));
		this.file.createNewFile();
	}
	
	@Test
	public void testChange() throws IOException{
		
		Thread t = new Thread(this.monitor);
		t.start();
		
		
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
		
		t.interrupt();
		
		Assert.assertTrue(o.changeNotified);
		
	}
	
	@Test
	public void testDelete() throws IOException{
		
		Thread t = new Thread(this.monitor);
		t.start();
		
		this.file.delete();
		
		// make a short break because the file monitor has a intervall
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t.interrupt();

		Assert.assertTrue(o.deleteNotified);
		
	}
	
}
