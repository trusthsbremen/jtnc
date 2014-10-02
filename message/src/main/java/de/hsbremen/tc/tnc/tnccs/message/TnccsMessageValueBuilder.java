package de.hsbremen.tc.tnc.tnccs.message;

import org.ietf.nea.pb.exception.RuleException;

public interface TnccsMessageValueBuilder {

	public abstract TnccsMessageValue toValue() throws RuleException;

	public abstract TnccsMessageValueBuilder clear();
}
