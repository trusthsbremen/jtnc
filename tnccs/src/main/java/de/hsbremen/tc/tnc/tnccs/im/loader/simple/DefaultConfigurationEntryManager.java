package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryManager;
import de.hsbremen.tc.tnc.tnccs.im.loader.FileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.TncConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

public class DefaultConfigurationEntryManager implements FileChangeListener, ConfigurationEntryManager{

	private Map<ConfigurationLineClassifier,Set<ConfigurationEntry>> configurationEntries;
	private final TncConfigurationFileParser fileFilter;
	private final Map<ConfigurationChangeListener, Set<ConfigurationLineClassifier>> listeners;
	
	public DefaultConfigurationEntryManager(TncConfigurationFileParser fileFilter, Map<ConfigurationChangeListener, Set<ConfigurationLineClassifier>> listeners){
		this.configurationEntries = new HashMap<>();
		this.listeners = listeners;
		for (Entry<ConfigurationChangeListener, Set<ConfigurationLineClassifier>> entry: this.listeners.entrySet()) {
			for (ConfigurationLineClassifier classifier : entry.getValue()) {
				if(!this.configurationEntries.containsKey(classifier)){
					this.configurationEntries.put(classifier, new HashSet<ConfigurationEntry>());
				}
			}
		}
		this.fileFilter = fileFilter;
	}
	
	@Override
	public void notifyChange(File config) {
		for  (Entry<ConfigurationLineClassifier,Set<ConfigurationEntry>> entry: this.configurationEntries.entrySet()) {
			
			Set<ConfigurationEntry> oldLines = entry.getValue();
			Set<ConfigurationEntry> newLines = this.fileFilter.parseConfigurationEntries(config, entry.getKey());
			
			if(newLines != null && !oldLines.equals(newLines)){
	//			List<ConfigurationEntry> newEntries = new LinkedList<>(lines);
	//			newEntries.removeAll(javaIm);
	//			List<ConfigurationEntry> removeableEntries = new LinkedList<>(javaIm);
	//			removeableEntries.removeAll(lines);
				
				this.configurationEntries.put(entry.getKey(), newLines);
				this.notifyChange(newLines,entry.getKey());
				
				// TODO now we have to load and/or unload IMC/V according to the two lists;
				// TODO make a number pool of IDs from unloaded IMC/V which can be reused;
				// TODO to remove IMC/V once they are idle mark them for removal and let another party decide when they should be removed;
				// TODO the other party needs to know how much connections are using the IMC;
			}
		}
	}

	@Override
	public void notifyDelete(File config) {
		for (Set<ConfigurationEntry> entries : this.configurationEntries.values()) {
			entries.clear();
		}
		this.notifyDelete();
	}

	private void notifyDelete(){
		for (ConfigurationChangeListener listener : this.listeners.keySet()) {
			listener.notifyDelete();
		}
	}
	
	private void notifyChange(Set<ConfigurationEntry> entries, ConfigurationLineClassifier classifier){
		for (Entry<ConfigurationChangeListener, Set<ConfigurationLineClassifier>> entry: this.listeners.entrySet()) {
			if(entry.getValue().contains(classifier)){
				entry.getKey().notifyChange(entries, classifier);
			}
		}
	}
	
}
