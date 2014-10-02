package de.hsbremen.tc.tnc.tnccs.message;

import org.ietf.nea.pb.exception.RuleException;

public interface TnccsMessageSubValueBuilder {

	public abstract TnccsMessageSubValue toValue() throws RuleException;

	public abstract TnccsMessageSubValueBuilder clear();
}
