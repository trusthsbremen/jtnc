package de.hsbremen.tc.tnc.im.adapter.tncs;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

public interface TncsAdapterFactory {

	public abstract TncsAdapter createTncsAdapter(IMV imv, TNCS tncs);

}