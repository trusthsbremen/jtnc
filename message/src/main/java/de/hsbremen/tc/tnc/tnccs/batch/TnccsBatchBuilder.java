package de.hsbremen.tc.tnc.tnccs.batch;

import org.ietf.nea.pb.exception.RuleException;

public interface TnccsBatchBuilder {

	public abstract TnccsBatch toBatch() throws RuleException;

	public abstract TnccsBatchBuilder clear();
}