package de.hsbremen.tc.tnc.adapter.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;

public class ImcAdapterFactoryIetf implements ImcAdapterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcAdapterFactoryIetf.class);
	
	@Override
	public ImcAdapter createImcAdapter(IMC imc, long primaryId) {
		
		if(imc == null){
			throw new NullPointerException("IMC cannot be null.");
		}
		
		ImcAdapter adapter = new ImcAdapterIetf(imc, primaryId);
		
		if(LOGGER.isDebugEnabled()){
			StringBuilder b = new StringBuilder();
			b.append("IMC adapter created for IMC ")
			.append(imc.toString())
			.append(" with the following supported abilities: \n")
			.append("Attribut support: ").append((imc instanceof AttributeSupport)).append("\n")
			.append("Long support: ").append((imc instanceof IMCLong)).append("\n");
			LOGGER.debug(b.toString());
		}
		
		return adapter;
	}
}
