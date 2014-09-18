package de.hsbremen.tc.tnc.im.loader;

import java.io.File;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.loader.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.loader.util.JarLoader;
import de.hsbremen.tc.tnc.im.loader.util.TncConfigFilter;

public class SimpleImcLoader implements ImLoader<IMC>{
	
	private static final ImTypeEnum TYPE = ImTypeEnum.JAVA_IMC;
	private final File tncConfig;
	
	public SimpleImcLoader(String path){
		this.tncConfig = new File(path);
	}
	
	@Override
	public List<IMC> loadImlist() {
		List<String> lines = TncConfigFilter.getLines(this.tncConfig, TYPE.classifier());
		List<IMC> imList = JarLoader.loadImcList(lines);
		
		return imList;
	}
}
