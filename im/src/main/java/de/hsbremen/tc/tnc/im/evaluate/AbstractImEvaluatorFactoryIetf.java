package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.TnccsAdapter;

public abstract class AbstractImEvaluatorFactoryIetf implements ImEvaluatorFactory{

	private ImEvaluatorManager units;
	
	protected AbstractImEvaluatorFactoryIetf(){
		this.units = null;
	}
	
	@Override
	public ImEvaluatorManager  getEvaluators(TnccsAdapter tncc, ImParameter imParams){
		if(this.units == null){
			this.units = this.createEvaluatorManager(tncc,imParams);
		}
		
		return this.units;
	}
	
	protected abstract ImEvaluatorManager createEvaluatorManager(TnccsAdapter tncc, ImParameter imParams);
}
