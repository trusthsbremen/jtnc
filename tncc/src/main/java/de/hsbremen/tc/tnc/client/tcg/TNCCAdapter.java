package de.hsbremen.tc.tnc.client.tcg;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.client.TncClient;

public class TNCCAdapter implements TNCC{

	TncClient tncc;
	
	@Override
	public void reportMessageTypes(IMC imc, long[] supportedTypes)
			throws TNCException {
		tncc.
		
	}

	@Override
	public void requestHandshakeRetry(IMC imc, long reason) throws TNCException {
		// TODO Auto-generated method stub
		
	}

}
