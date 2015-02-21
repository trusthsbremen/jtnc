package de.hsbremen.tc.tnc.message.tnccs.serialize;

import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.TnccsData;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;

public interface TnccsBatchContainer extends TnccsData{
	
	public List<ValidationException> getExceptions();
	
	public TnccsBatch getResult();
}
