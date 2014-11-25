package de.hsbremen.tc.tnc.im;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapterFactory;

public class ImcFactory {

	private final ImcAdapterFactory adapterFactory;

	public ImcFactory(ImcAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	public Imc createImc(long id, IMC imc, TNCC tncc){
	
		ImAttributes attributes = new ImAttributes(id);
		ImcAdapter imA = adapterFactory.createImcAdapter(imc);
		Imc im = new Imc(attributes, imA);
		
		return im;
	}
}
