package de.hsbremen.tc.tnc.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.attribute.Attributed;

public interface TnccAdapterFactory {
	public TNCC createTncc(IMC imc, Attributed attributes);
}