package de.hsbremen.tc.tnc.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.newp.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.newp.manager.ImManager;

public class TnccAdapterFactoryIetf implements TnccAdapterFactory {

	private final GlobalHandshakeRetryListener listener;

	public TnccAdapterFactoryIetf(GlobalHandshakeRetryListener listener) {
		this.listener = listener;
	}

	@Override
	public TNCC createTncc(IMC imc, Attributed attributes, ImManager<IMC> manager){
		
		if(imc instanceof IMCLong){
			return new TnccAdapterIetfLong(manager,attributes,this.listener);
		}else{
			return new TnccAdapterIetf(manager, attributes, this.listener);
		}
		
	}
	
}
