package org.ietf.nea.pb.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class PbBatchContainer implements TnccsBatchContainer {

	private final PbBatch batch;
	
	private final List<ValidationException> exceptions;

	public PbBatchContainer(PbBatch batch, List<ValidationException> exceptions) {
		
		if(batch == null){
			throw new NullPointerException("Batch cannot be null.");
		}
		
		this.batch = batch;
		this.exceptions = ((exceptions != null) ? exceptions : new ArrayList<ValidationException>(0));
	}

	@Override
	public List<ValidationException> getExceptions() {
		return Collections.unmodifiableList(this.exceptions);
	}

	@Override
	public PbBatch getResult() {
		return this.batch;
	}

}
