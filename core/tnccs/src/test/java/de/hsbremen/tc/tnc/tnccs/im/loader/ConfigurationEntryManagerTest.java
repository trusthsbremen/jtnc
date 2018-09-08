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

import de.hsbremen.tc.tnc.tnccs.im.loader.Dummy.NotifyTestObject;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileParserImJava;

public class ConfigurationEntryManagerTest {

	private NotifyTestObject o;
	private ConfigurationFileChangeMonitor monitor;
	private File file = new File("src/test/resources/tnc_config.test");

	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp() throws IOException{
		o = new Dummy.NotifyTestObject();
		ConfigurationEntryHandler handler = Dummy.getConfigurationChangeListener(o);
        ConfigurationFileParser parser = new DefaultConfigurationFileParserImJava(false);
        DefaultConfigurationFileChangeListener listener = new DefaultConfigurationFileChangeListener(parser);
        listener.addHandler(handler.getSupportedConfigurationLines(), handler);
        this.monitor = new DefaultConfigurationFileChangeMonitor(file, 200, true);
        this.monitor.add(listener);
        this.file.createNewFile();

	}
	
	@Test
	public void testChange() throws IOException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test change notification."));
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
			System.err.println(e.getMessage());
		}
		
		this.monitor.stop();
		
		Assert.assertTrue(o.changeNotified);
		
	}
	
	@Test
	public void testDelete() throws IOException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test delete notification."));
		
		this.monitor.start();
		
		this.file.delete();
		
		// make a short break because the file monitor has a intervall
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		
		this.monitor.stop();

		Assert.assertTrue(o.deleteNotified);
		
	}
	
	@Test
	public void testIgnoreChange() throws IOException{
		
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test ignore change notification."));
		
		this.monitor.start();
		
		
		FileWriter w = new FileWriter(this.file);
		w.append(Dummy.getConfigLineComment());
		w.flush();
		w.close();
		
		// make a short break because the file monitor has a intervall
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		
		this.monitor.stop();
		
		Assert.assertFalse(o.changeNotified);
		
	}
	
	@Test
	public void testLineDeleteChange() throws IOException{
		
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test line delete change notification."));
		
		this.monitor.start();
		
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
		}
		
		this.monitor.stop();
		
		Assert.assertTrue(o.changeNotified);
		
	}
	
	
}
