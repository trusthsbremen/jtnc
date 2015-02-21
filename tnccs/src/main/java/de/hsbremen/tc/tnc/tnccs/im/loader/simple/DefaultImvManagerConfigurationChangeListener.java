package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.DefaultConfigurationLineClassifierEnum;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

public class DefaultImvManagerConfigurationChangeListener extends AbstractImManagerConfigurationChangeListener<IMV> {

    private static final Set<ConfigurationLineClassifier> CLASSIFIER =
            new HashSet<>(Arrays.asList(new ConfigurationLineClassifier[]{
           DefaultConfigurationLineClassifierEnum.JAVA_IMV}));

    public DefaultImvManagerConfigurationChangeListener(
            final ImLoader<IMV> imvLoader, final ImManager<IMV> manager) {
       super(imvLoader, manager);
    }

    @Override
    public Set<ConfigurationLineClassifier> getSupportedConfigurationLines() {
        return Collections.unmodifiableSet(CLASSIFIER);
    }

}
