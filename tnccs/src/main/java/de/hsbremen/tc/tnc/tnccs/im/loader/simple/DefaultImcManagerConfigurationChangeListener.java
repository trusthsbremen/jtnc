package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

public class DefaultImcManagerConfigurationChangeListener extends AbstractImManagerConfigurationChangeListener<IMC> implements
        ConfigurationEntryChangeListener {

    private static final Set<ConfigurationLineClassifier> CLASSIFIER = new HashSet<>(
            Arrays.asList(new ConfigurationLineClassifier[] { DefaultConfigurationLineClassifierEnum.JAVA_IMC }));

    public DefaultImcManagerConfigurationChangeListener(
            final ImLoader<IMC> imcLoader, final ImManager<IMC> manager) {
        super(imcLoader, manager);
    }

    @Override
    public Set<ConfigurationLineClassifier> getSupportedConfigurationLines() {
        return Collections.unmodifiableSet(CLASSIFIER);
    }

}
