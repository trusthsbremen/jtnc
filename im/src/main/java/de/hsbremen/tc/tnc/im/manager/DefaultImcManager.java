package de.hsbremen.tc.tnc.im.manager;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.container.DefaultImContainer;
import de.hsbremen.tc.tnc.im.container.ImContainer;
import de.hsbremen.tc.tnc.im.container.SupportedMessageType;
import de.hsbremen.tc.tnc.im.loader.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.loader.util.JarLoader;
import de.hsbremen.tc.tnc.im.loader.util.TncConfigFilter;

public class DefaultImcManager implements ImManager<IMC> {
	private static final ImTypeEnum TYPE = ImTypeEnum.JAVA_IMC;
	
	private final Map<IMC,ImContainer<IMC>> imcs;
	
	public DefaultImcManager(){
		this.imcs = new LinkedHashMap<>();
	}
	
	@Override
	public void loadAll(File tncConfig) {
		List<String> lines = TncConfigFilter.getLines(tncConfig, TYPE.classifier());
		List<IMC> imList = JarLoader.loadImList(lines);
		this.imcs.clear();
		for (IMC imc : imList) {
			
			imc.initialize(tncc);
		}
	}

	@Override
	public List<ImContainer<IMC>> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImContainer<IMC> get(IMC searched) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reportMessageTypes(IMC searched, List<SupportedMessageType> types) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reserverImcId(IMC searched) {
		// TODO Auto-generated method stub
		
	}

}
