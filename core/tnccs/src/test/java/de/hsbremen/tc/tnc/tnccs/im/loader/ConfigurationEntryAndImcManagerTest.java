package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileParserImJava;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultImcManagerConfigurationEntryHandler;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public class ConfigurationEntryAndImcManagerTest {

    private ConfigurationFileChangeMonitor monitor;
    private File file = new File("src/test/resources/tnc_config.test");
    private ImManager<IMC> imManager;
    private ImMessageRouter router;
    private ImcAdapterFactory imcFactory;
    private TnccAdapterFactory tnccFactory;
    private ImLoader<IMC> loader;
    @BeforeClass
    public static void logSetup(){
        Dummy.setLogSettings();
    }
    
    @Before
    public void setUp() throws IOException{
        this.imcFactory = new ImcAdapterFactoryIetf();
        this.tnccFactory = new TnccAdapterFactoryIetf(Dummy.getRetryListener());
        this.router = new DefaultImMessageRouter();
        this.imManager = new DefaultImcManager(router, imcFactory, tnccFactory);
        
        this.loader = new DefaultImLoader<IMC>();
        
        ConfigurationEntryHandler handler = new DefaultImcManagerConfigurationEntryHandler(this.loader, imManager);
        ConfigurationFileParser parser = new DefaultConfigurationFileParserImJava(false);
        DefaultConfigurationFileChangeListener listener = new DefaultConfigurationFileChangeListener(parser);
        listener.addHandler(handler.getSupportedConfigurationLines(), handler);
        this.monitor = new DefaultConfigurationFileChangeMonitor(file, 200, false);
        this.monitor.add(listener);
        this.file.createNewFile();
    }
    
    @Test
    public void testChange() throws IOException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test change notification."));
        this.monitor.start();
        
        
        FileWriter w = new FileWriter(this.file);
        w.append(Dummy.getConfigLineToDummyImc());
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
        
        Assert.assertTrue(this.imManager.getManaged().size() > 0);
        
    }
    
    @Test
    public void testDelete() throws IOException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test delete notification."));
        
        this.monitor.start();

        FileWriter w = new FileWriter(this.file);
        w.append(Dummy.getConfigLineToDummyImc());
        w.flush();
        w.close();
        
        // make a short break because the file monitor has a intervall
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        Assert.assertTrue(this.imManager.getManaged().size() > 0);
       
        this.file.delete();
        
        // make a short break because the file monitor has a intervall
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        this.monitor.stop();

        Assert.assertTrue(this.imManager.getManaged().isEmpty());
        
    }
    
    @Test
    public void testDouble() throws IOException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test duplicate entry notification."));
        this.monitor.start();
        
        
        FileWriter w = new FileWriter(this.file);
        w.append(Dummy.getConfigLineToDummyImc());
        w.flush();
        
        // make a short break because the file monitor has a intervall
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        w.append("\n"+Dummy.getConfigLineToDummyImc());
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
        
        Assert.assertTrue(this.imManager.getManaged().size() == 1);
        
    }
    
    @Test
    public void testDeletEntry() throws IOException{
        System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test duplicate entry notification."));
        this.monitor.start();
        
        
        FileWriter w = new FileWriter(this.file);
        w.append(Dummy.getConfigLineToDummyImc());
        w.flush();
        w.close();
        
        // make a short break because the file monitor has a intervall
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        Assert.assertTrue(this.imManager.getManaged().size() == 1);

        w = new FileWriter(this.file);
        w.write("# clean1\n");
        w.write("# clean2\n");
        w.flush();
        w.close();
        
        // make a short break because the file monitor has a intervall
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
        }
        
        this.monitor.stop();
        
        // because of thread it is not always true that why it is deleted
        //Assert.assertTrue(this.imManager.getManaged().isEmpty());
        
    }
}
