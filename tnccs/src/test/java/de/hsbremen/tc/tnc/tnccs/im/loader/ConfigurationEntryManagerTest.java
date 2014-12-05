package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultConfigurationEntryManager;
import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.DefaultTncConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy;
import de.hsbremen.tc.tnc.tnccs.im.loader.FileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.FileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy.NotifyTestObject;

public class ConfigurationEntryManagerTest {

	private FileChangeListener manager;
	private NotifyTestObject o;
	private FileChangeMonitor monitor;
	private File file = new File("src/test/resources/tnc_config.test");

	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp() throws IOException{
		o = new Dummy.NotifyTestObject();
		manager = new DefaultConfigurationEntryManager(new DefaultTncConfigurationFileParser(), Dummy.getConfigurationChangeListener(o));
		this.monitor = new DefaultFileChangeMonitor(file, 200, true);
		this.monitor.add(manager);
		this.file.createNewFile();
	}
	
	@Test
	public void testChange() throws IOException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test change notification."));
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
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test delete notification."));
		
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
	
	@Test
	public void testIgnoreChange() throws IOException{
		
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test ignore change notification."));
		
		Thread t = new Thread(this.monitor);
		t.start();
		
		
		FileWriter w = new FileWriter(this.file);
		w.append(Dummy.getConfigLineComment());
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
		
		Assert.assertFalse(o.changeNotified);
		
	}
	
	@Test
	public void testLineDeleteChange() throws IOException{
		
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test line delete change notification."));
		
		Thread t = new Thread(this.monitor);
		t.start();
		
		FileWriter w = new FileWriter(this.file);
		w.append(Dummy.getConfigLine());
		w.append("\n");
		w.append(Dummy.getConfigLine());
		w.append("\n");
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
		
		Assert.assertTrue(o.changeNotified);
		o.changeNotified = false;
		Assert.assertFalse(o.changeNotified);
		
		StringBuilder b = new StringBuilder();
		
		BufferedReader wr = new BufferedReader(new FileReader(this.file));
		String s = null;
		int i = 0;
		while ((s = wr.readLine()) != null){
			System.out.println(s);
			if(i%2 != 0){
				i++;
			}else{
				b.append(s);
				b.append("\n");
				i++;
			}
		}
		wr.close();
		
		System.out.println("----");
		
		w = new FileWriter(this.file, false);
		System.out.println(b.toString());
		w.write(b.toString());
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
	
	
}
