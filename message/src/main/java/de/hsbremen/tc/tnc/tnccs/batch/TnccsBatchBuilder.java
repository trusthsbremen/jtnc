package de.hsbremen.tc.tnc.tnccs.batch;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsBatchBuilder {

	public abstract TnccsBatch toBatch() throws ValidationException;

}