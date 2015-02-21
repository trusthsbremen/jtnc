package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.ArrayList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.util.NotNull;

public class DefaultImcLoader implements ImLoader<IMC> {

    @Override
    public List<IMC> loadIms(List<DefaultConfigurationEntryImJava> configs) {
        NotNull.check("Configuration entrys cannot be null.", configs);
        List<IMC> imList = new ArrayList<>();

        for (DefaultConfigurationEntryImJava config : configs) {
            IMC im = this.loadIm(config);
            if (im != null) {
                imList.add(im);
            }
        }

        return imList;
    }

    @Override
    public IMC loadIm(DefaultConfigurationEntryImJava config) {
        NotNull.check("Configuration entry cannot be null.", config);
        IMC im = JarLoaderUtil.loadIm(config);

        return im;
    }

}
