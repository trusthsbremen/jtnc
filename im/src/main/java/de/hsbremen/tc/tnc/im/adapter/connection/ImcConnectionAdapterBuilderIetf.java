package de.hsbremen.tc.tnc.im.adapter.connection;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;

import de.hsbremen.tc.tnc.im.handler.ImConnectionMessageQueue;


public class ImcConnectionAdapterBuilderIetf implements
		ImcConnectionAdapterBuilder {

	@Override
	public IMCConnection buildAdapter(IMC type,
			ImConnectionMessageQueue observer) {
		
		IMCConnection con = new ImcConnectionAdapterIetf(observer);
		
		if(type instanceof IMCLong){	
			return new ImcConnectionAdapterIetfLong(con,observer);
		}
		
		// TODO add SOH here 
		
		return con;
		
		
	}

}
