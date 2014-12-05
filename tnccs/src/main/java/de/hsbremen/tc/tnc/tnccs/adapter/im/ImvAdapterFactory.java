package de.hsbremen.tc.tnc.tnccs.adapter.im;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

public interface ImvAdapterFactory {

	public abstract ImvAdapter createImvAdapter(
			IMV imv, long primaryId);

}