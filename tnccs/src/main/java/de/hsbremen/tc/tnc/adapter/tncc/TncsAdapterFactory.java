package de.hsbremen.tc.tnc.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.newp.manager.ImManager;

public interface TncsAdapterFactory {
	public TNCS createTncs(IMV imc, Attributed attributes, ImManager<IMV> manager);
}