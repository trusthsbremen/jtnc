package de.hsbremen.tc.tnc.im.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

public interface TnccAdapterFactory {

	public abstract TnccAdapter createTnccAdapter(IMC imc, TNCC tncc);

}