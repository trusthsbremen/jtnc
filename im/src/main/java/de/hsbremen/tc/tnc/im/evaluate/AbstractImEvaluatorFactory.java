package de.hsbremen.tc.tnc.im.evaluate;

import java.util.HashMap;
import java.util.Map;

import de.hsbremen.tc.tnc.im.adapter.imc.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;

public abstract class AbstractImEvaluatorFactory implements ImEvaluatorFactory{

	private Map<Long,ImEvaluator> units;
	
	protected AbstractImEvaluatorFactory(){
		this.units = new HashMap<Long, ImEvaluator>();
	}

	@Override
	public Map<Long, ImEvaluator> getUnits(TnccAdapter tncc, ImParameter imParams){
		if(this.units == null || this.units.isEmpty()){
			this.units = this.createUnits(tncc,imParams);
		}
		
		return this.units;
	}

	
	protected abstract Map<Long,ImEvaluator> createUnits(TnccAdapter tncc, ImParameter imParams);
	
}
