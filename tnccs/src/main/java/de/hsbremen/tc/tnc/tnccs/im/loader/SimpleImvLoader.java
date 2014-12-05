package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.ArrayList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

public class SimpleImvLoader implements ImLoader<IMV>{

	@Override
	public List<IMV> loadIms(List<ImConfigurationEntry> configs) {
		List<IMV> imList = new ArrayList<>();
		if(configs != null){
			for (ImConfigurationEntry config : configs) {
				IMV im = this.loadIm(config);
				if(im != null){
					imList.add(im);
				}
			}
		}
		return imList;
	}

	@Override
	public IMV loadIm(ImConfigurationEntry config) {
		IMV im = null;
		if(config != null){
			im = JarLoader.loadIm(config);
		}
		
		return im;
	}

}
