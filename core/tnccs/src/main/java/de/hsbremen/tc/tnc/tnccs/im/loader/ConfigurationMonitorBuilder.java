package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;

public interface ConfigurationMonitorBuilder {

    public abstract ConfigurationMonitorBuilder setParser(
            ConfigurationFileParser parser);

    public abstract ConfigurationMonitorBuilder addChangeListener(
            ConfigurationEntryChangeListener listener);

    public abstract ConfigurationFileChangeMonitor createMonitor(File f);

}