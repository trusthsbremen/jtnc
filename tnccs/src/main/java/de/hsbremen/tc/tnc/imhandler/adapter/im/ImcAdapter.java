package de.hsbremen.tc.tnc.imhandler.adapter.im;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;

public class ImcAdapter {
	
	
	
	
	public void test() throws TncException{
	
		IMC imc = null;
		IMCLong imcl = null;
		IMCConnection imcc = null;
		TNCC tncc = null;
		
		IMV imv = null;
		IMVConnection imvc = null;
		
		try{
			imc.initialize(tncc);
			imc.notifyConnectionChange(imcc, DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN.state());
			imc.receiveMessage(imcc, 1L, new byte[0]);
			imcl.receiveMessageLong(imcc, 0, 0, 0, new byte[0], 0, 0);
			imc.batchEnding(imcc);
			imc.terminate();
			
			imc.beginHandshake(imcc); // IMC and IMVTNCSFirst only
		
			imv.solicitRecommendation(imvc); // IMV only
			
		}catch(TNCException e){
			throw new TncException(e);
		} catch (org.trustedcomputinggroup.tnc.ifimv.TNCException e) {
			throw new TncException(e);
		}
		
	
	}
	
	
	
	
}
