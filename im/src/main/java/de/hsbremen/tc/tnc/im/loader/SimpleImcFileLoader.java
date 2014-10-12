package de.hsbremen.tc.tnc.im.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.loader.enums.ImTypeEnum;

public class SimpleImcFileLoader implements ImFileLoader<IMC>{
	
	private static final ImTypeEnum TYPE = ImTypeEnum.JAVA_IMC;
	private final File tncConfig;
	
	public SimpleImcFileLoader(String path){
		this.tncConfig = new File(path);
	}
	
	@Override
	public List<IMC> loadImlist() {
		List<String> lines = TncConfigFileFilter.getLines(this.tncConfig, TYPE.classifier());
		List<ImLoadParameter> params = TncConfigFileFilter.convertLinesToLoadingList(lines);
		List<IMC> imList = new ArrayList<>(params.size());
		for (ImLoadParameter param : params) {
			IMC im = JarLoader.loadIm(param);
			if(im != null){
				imList.add(im);
			}
		}

		return imList;
	}
}
