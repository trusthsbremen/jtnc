package de.hsbremen.tc.tnc.tnccs.message;


public interface TnccsMessageValueBuilder {

	public abstract TnccsMessageValue toValue();

	public abstract TnccsMessageValueBuilder clear();
}
