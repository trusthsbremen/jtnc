package de.hsbremen.tc.tnc.tnccs.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public interface TnccsMessageSubValueBuilder {

	public abstract TnccsMessageSubValue toValue() throws ValidationException;

	public abstract TnccsMessageSubValueBuilder clear();
}
