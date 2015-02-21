package org.ietf.nea.pb.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;

public class DefaultTnccsBatchContainer implements TnccsBatchContainer {

	private final TnccsBatch batch;
	
	private final List<ValidationException> exceptions;

	public DefaultTnccsBatchContainer(TnccsBatch batch, List<ValidationException> exceptions) {
		
		this.batch = batch;
		this.exceptions = ((exceptions != null) ? exceptions : new ArrayList<ValidationException>(0));
	}

	@Override
	public List<ValidationException> getExceptions() {
		return Collections.unmodifiableList(this.exceptions);
	}

	@Override
	public TnccsBatch getResult() {
		return this.batch;
	}

}
