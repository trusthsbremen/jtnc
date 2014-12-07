package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.ArrayList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.tnccs.im.loader.ImConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;

public class SimpleImcLoader implements ImLoader<IMC>{

	@Override
	public List<IMC> loadIms(List<ImConfigurationEntry> configs) {
		List<IMC> imList = new ArrayList<>();
		if(configs != null){
			for (ImConfigurationEntry config : configs) {
				IMC im = this.loadIm(config);
				if(im != null){
					imList.add(im);
				}
			}
		}
		return imList;
	}

	@Override
	public IMC loadIm(ImConfigurationEntry config) {
		IMC im = null;
		if(config != null){
			im = JarLoaderUtil.loadIm(config);
		}
		
		return im;
	}

}
