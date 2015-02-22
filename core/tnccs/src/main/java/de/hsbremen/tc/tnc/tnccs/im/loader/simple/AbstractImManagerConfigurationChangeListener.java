package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.exception.LoadingException;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;

public abstract class AbstractImManagerConfigurationChangeListener<T> implements ConfigurationEntryChangeListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractImManagerConfigurationChangeListener.class);
    
    final ImLoader<T> imLoader;
    final ImManager<T> manager;
    final Map<ConfigurationEntry, Long> loadedEntries;

    public AbstractImManagerConfigurationChangeListener(
            final ImLoader<T> imLoader, final ImManager<T> manager) {
        this.imLoader = imLoader;
        this.manager = manager;
        this.loadedEntries = new ConcurrentHashMap<>();
    }

    @Override
    public void notifyDelete() {
        this.manager.terminate();
    }

    @Override
    public void notifyChange(final Set<ConfigurationEntry> entries, final ConfigurationLineClassifier classfier) {
        Map<Long, T> imcs = this.manager.getManaged();
    
        List<ConfigurationEntry> removeableEntries = new LinkedList<>(
                loadedEntries.keySet());
        removeableEntries.removeAll(entries);
    
        for (ConfigurationEntry configurationEntry : removeableEntries) {
            if (this.loadedEntries.containsKey(configurationEntry)) {
                Long primaryId = this.loadedEntries.get(configurationEntry);
                LOGGER.debug("Configuration entry "
                        + configurationEntry.toString()
                        + " was removed, remove IM(C/V) with ID "
                        + primaryId.toString() + " from manager.");
                this.manager.remove(primaryId);
                this.loadedEntries.remove(configurationEntry);
            }
        }
    
        this.imLoader.cleanUp(new HashSet<>(this.loadedEntries.keySet()));
        
        for (ConfigurationEntry configurationEntry : entries) {
            if (configurationEntry instanceof DefaultConfigurationEntryImJava) {
                if (!loadedEntries.containsKey(configurationEntry)) {
    
                      this.addImcToManager(configurationEntry);
    
                } else {
                    if (!imcs.containsKey(this.loadedEntries
                            .get(configurationEntry))) {
    
                        LOGGER.debug("Configuration entry "
                                + configurationEntry.toString()
                                + " is old but IM(C/V) was removed from manager."
                                + " IM(C/V) will be added to manager again.");
    
                        this.addImcToManager(configurationEntry);
    
                    } else {
                        LOGGER.debug("IM(C/V) already running "
                                + "and will not be added again.");
                    }
                }
            }
    
        }
    
    }

    private void addImcToManager(ConfigurationEntry configurationEntry) {
        try {
            T imc = this.imLoader
                .loadIm((DefaultConfigurationEntryImJava) configurationEntry);

            Long primaryId = this.manager.add(imc);
            this.loadedEntries.put(configurationEntry, primaryId);
    
            LOGGER.debug("Configuration entry "
                    + configurationEntry.toString()
                    + " is new. IM(C/V) with ID " + primaryId
                    + " was added to manager.");
        } catch (ImInitializeException e) {
            LOGGER.error("IM(C/V) could not be initialized and"
                    + " will be ignored.", e);

        } catch (LoadingException e) {
            LOGGER.error("IM(C/V) could not be loaded and"
                    + " will be ignored.", e);
        }
    }

}