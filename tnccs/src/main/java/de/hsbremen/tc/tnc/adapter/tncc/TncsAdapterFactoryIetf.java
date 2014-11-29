package de.hsbremen.tc.tnc.adapter.tncc;

import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.newp.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.newp.manager.ImManager;

public class TncsAdapterFactoryIetf implements TncsAdapterFactory {

	private final GlobalHandshakeRetryListener listener;

	public TncsAdapterFactoryIetf(GlobalHandshakeRetryListener listener) {
		this.listener = listener;
	}

	@Override
	public TNCS createTncs(IMV imc, Attributed attributes, ImManager<IMV> manager){
		
		if(imc instanceof IMVLong){
			return new TncsAdapterIetfLong(manager,attributes,this.listener);
		}else{
			return new TncsAdapterIetf(manager, attributes, this.listener);
		}
		
	}
	
}
