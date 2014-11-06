package de.hsbremen.tc.tnc.imhandler.module;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

public class TnccsImcModuleHolderBuilderIetf implements TnccsImModuleHolderBuilder<IMC>{

	@Override
	public TnccsImModuleHolder<IMC> createImModule(long primaryId, IMC imc) {
		return new DefaultTnccsImModuleHolder<IMC>(primaryId, imc);
	}


}
