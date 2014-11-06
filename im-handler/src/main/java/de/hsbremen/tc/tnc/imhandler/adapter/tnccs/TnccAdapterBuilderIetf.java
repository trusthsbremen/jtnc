package de.hsbremen.tc.tnc.imhandler.adapter.tnccs;

import java.util.Collection;
import java.util.Iterator;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.IMCSOH;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.imhandler.manager.ImModuleManager;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;


public class TnccAdapterBuilderIetf implements TnccAdapterBuilder{
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.TnccAdapterBuilder#buildAdapter(de.hsbremen.tc.tnc.im.manager.ImModuleManager)
	 */
	@Override
	public TNCC buildAdapter(ImModuleManager<IMC> manager) {

		Collection<TnccsImModuleHolder<IMC>> imcs = manager.getAll();
		
		// got thru the list and look if special functions such as long or SOH are needed
		boolean hasLong = false;
		boolean hasSoh = false;
		for(Iterator<TnccsImModuleHolder<IMC>> iter = imcs.iterator(); iter.hasNext() && (hasLong == false || hasSoh == false);){
			IMC imc = iter.next().getIm();
			if (imc instanceof IMCLong) {
				hasLong = true;
			}
			
			if (imc instanceof IMCSOH) {
				 hasSoh = true;
			}
		}
		
		TNCC tncc = new TnccAdapterIetf(manager);
	
		if(hasLong){
			return new TnccAdapterIetfLong(tncc, manager);
		}
		
		// TODO add SOH here 
		
		return tncc;

	}

}
