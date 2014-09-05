package de.hsbremen.tc.tnc.imc;

import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

public class ImcLongFacade implements IMCLong, IMC {

	@Override
	public void initialize(TNCC tncc) throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminate() throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyConnectionChange(IMCConnection c, long newState)
			throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginHandshake(IMCConnection c) throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(IMCConnection c, long messageType, byte[] message)
			throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void batchEnding(IMCConnection c) throws TNCException {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessageLong(IMCConnection c, long messageFlags,
			long messageVendorID, long messageSubtype, byte[] message,
			long sourceIMVID, long destinationIMCID) throws TNCException {
		// TODO Auto-generated method stub

	}

}
