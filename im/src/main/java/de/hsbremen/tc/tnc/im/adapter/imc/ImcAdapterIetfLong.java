package de.hsbremen.tc.tnc.im.adapter.imc;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;

public class ImcAdapterIetfLong implements IMC, IMCLong, AttributeSupport{

	private final ImcAdapter stdImc;
	
	public ImcAdapterIetfLong(){
		this.stdImc = new ImcAdapterIetf();
	}
	
	@Override
	public void initialize(TNCC tncc) throws TNCException {
		this.stdImc.initialize(tncc);
	}

	@Override
	public void terminate() throws TNCException {
		this.stdImc.terminate();
		
	}

	@Override
	public void notifyConnectionChange(IMCConnection c, long newState)
			throws TNCException {
		this.stdImc.notifyConnectionChange(c, newState);
		
	}

	@Override
	public void beginHandshake(IMCConnection c) throws TNCException {
		this.stdImc.beginHandshake(c);
		
	}

	@Override
	public void receiveMessage(IMCConnection c, long messageType, byte[] message)
			throws TNCException {
		
		this.stdImc.receiveMessage(c, messageType, message);
		
	}

	@Override
	public void batchEnding(IMCConnection c) throws TNCException {
		this.stdImc.batchEnding(c);
		
	}

	@Override
	public Object getAttribute(long attributeID) throws TNCException {

		return this.stdImc.getAttribute(attributeID);
		
	}

	@Override
	public void setAttribute(long attributeID, Object attributeValue)
			throws TNCException {
		
		this.stdImc.setAttribute(attributeID, attributeValue);
		
	}

	@Override
	public void receiveMessageLong(IMCConnection c, long messageFlags,
			long messageVendorID, long messageSubtype, byte[] message,
			long sourceIMVID, long destinationIMCID) throws TNCException {
		
		this.stdImc.receiveMessage(ImComponentFactory.createRawComponent((byte)(messageFlags & 0xFF), messageVendorID, messageSubtype, destinationIMCID, sourceIMVID, message));
		
	}
}
