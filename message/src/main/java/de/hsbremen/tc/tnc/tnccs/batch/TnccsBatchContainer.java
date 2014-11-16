package de.hsbremen.tc.tnc.tnccs.batch;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.TnccsData;

public interface TnccsBatchContainer extends TnccsData{
	
	public List<ValidationException> getExceptions();
	
	public TnccsBatch getResult();
}
