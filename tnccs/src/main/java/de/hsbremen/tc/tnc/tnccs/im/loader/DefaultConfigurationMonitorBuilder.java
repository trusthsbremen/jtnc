package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeHandler;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileChangeMonitor;
import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationFileParserImJava;

public class DefaultConfigurationMonitorBuilder implements ConfigurationMonitorBuilder {

    private ConfigurationFileParser parser;
    private final Map<ConfigurationEntryChangeListener, Set<ConfigurationLineClassifier>> listeners;
    private final long interval;
    private final boolean paranoid;
    
    public DefaultConfigurationMonitorBuilder(){
        this(5000, false);
    }
    
    public DefaultConfigurationMonitorBuilder(long interval,
            boolean paranoid){
        this.interval = interval;
        this.paranoid = paranoid;
        this.listeners = new HashMap<>();

    }

    @Override
    public ConfigurationMonitorBuilder setParser(ConfigurationFileParser parser){
        if(parser != null){
            this.parser = parser;
        }
        return this;
    }

    @Override
    public ConfigurationMonitorBuilder addChangeListener(ConfigurationEntryChangeListener listener){
        if(listener != null){
            listeners.put(listener,listener.getSupportedConfigurationLines());
        }
        return this;
    }

    @Override
    public ConfigurationFileChangeMonitor createMonitor(File f){
        ConfigurationFileChangeHandler handler = new DefaultConfigurationFileChangeHandler(
                (this.parser != null) ? parser : new DefaultConfigurationFileParserImJava(),
                        listeners);
        
        ConfigurationFileChangeMonitor monitor = 
                new DefaultConfigurationFileChangeMonitor(f, interval, paranoid);
        monitor.add(handler);
        
        return monitor;
    }
     
}
