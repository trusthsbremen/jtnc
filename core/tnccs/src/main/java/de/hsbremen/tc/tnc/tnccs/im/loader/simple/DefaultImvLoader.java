package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.ArrayList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.util.NotNull;

public class DefaultImvLoader implements ImLoader<IMV> {

    @Override
    public List<IMV> loadIms(
            final List<DefaultConfigurationEntryImJava> configs) {
        NotNull.check("Configuration entrys cannot be null.", configs);
        List<IMV> imList = new ArrayList<>();

        for (DefaultConfigurationEntryImJava config : configs) {
            IMV im = this.loadIm(config);
            if (im != null) {
                imList.add(im);
            }
        }

        return imList;
    }

    @Override
    public IMV loadIm(final DefaultConfigurationEntryImJava config) {
        NotNull.check("Configuration entry cannot be null.", config);
        IMV im = JarLoaderUtil.loadIm(config);

        return im;
    }

}
