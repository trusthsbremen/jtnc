package de.hsbremen.tc.tnc.im.module;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

public class ImcModuleBuilderIetf implements ImModuleBuilder<IMC>{

	@Override
	public ImModule<IMC> createImModule(long primaryId, IMC imc) {
		return new DefaultImModule<IMC>(primaryId, imc);
	}


}
