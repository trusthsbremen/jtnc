package de.hsbremen.tc.tnc.tnccs.serialize;

import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.TnccsData;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public interface TnccsBatchContainer extends TnccsData{
	
	public List<ValidationException> getExceptions();
	
	public TnccsBatch getResult();
}
