package de.hsbremen.tc.tnc.adapter.im;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

public interface ImcAdapterFactory {

	public abstract ImcAdapter createImcAdapter(IMC imc, long primaryId);

}