package de.hsbremen.tc.tnc.im.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

public class TnccAdapterIetfFactory implements TnccAdapterFactory {

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactory#createTnccAdapter(org.trustedcomputinggroup.tnc.ifimc.IMC, org.trustedcomputinggroup.tnc.ifimc.TNCC)
	 */
	@Override
	public TnccAdapter createTnccAdapter(IMC imc,
			TNCC tncc) {
		if(imc == null){
			throw new NullPointerException("IMC cannot be null.");
		}
		
		if(tncc == null){
			throw new NullPointerException("TNCC cannot be null.");
		}
		
		return new TnccAdapterIetf(imc, tncc);
	}

}
