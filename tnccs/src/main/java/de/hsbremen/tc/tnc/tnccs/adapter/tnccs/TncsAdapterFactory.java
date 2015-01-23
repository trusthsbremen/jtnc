package de.hsbremen.tc.tnc.tnccs.adapter.tnccs;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

public interface TncsAdapterFactory {
	public TNCS createTncs(IMV imc, Attributed attributes, ImManager<IMV> manager);
}