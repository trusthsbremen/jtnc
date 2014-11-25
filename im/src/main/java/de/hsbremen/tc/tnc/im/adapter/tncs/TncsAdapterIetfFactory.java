package de.hsbremen.tc.tnc.im.adapter.tncs;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

public class TncsAdapterIetfFactory implements TncsAdapterFactory {

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactory#createTncsAdapter(org.trustedcomputinggroup.tnc.ifimv.IMV, org.trustedcomputinggroup.tnc.ifimv.TNCS)
	 */
	@Override
	public TncsAdapter createTncsAdapter(IMV imv,
			TNCS tncs) {
		if(imv == null){
			throw new NullPointerException("IMV cannot be null.");
		}
		
		if(tncs == null){
			throw new NullPointerException("TNCS cannot be null.");
		}
		
		return new TncsAdapterIetf(imv, tncs);
	}

}
