package de.hsbremen.tc.tnc.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.newp.ImManager;

public class TnccAdapterFactoryIetf implements TnccAdapterFactory {

	private final GlobalHandshakeRetryListener listener;
	private final ImManager<IMC> manager;

	public TnccAdapterFactoryIetf(GlobalHandshakeRetryListener listener,
			ImManager<IMC> manager) {
		this.listener = listener;
		this.manager = manager;
	}

	@Override
	public TNCC createTncc(IMC imc, Attributed attributes){
		
		if(imc instanceof IMCLong){
			return new TnccAdapterIetfLong(this.manager,attributes,this.listener);
		}else{
			return new TnccAdapterIetf(this.manager, attributes, this.listener);
		}
		
	}
	
}
