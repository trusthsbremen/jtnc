package de.hsbremen.tc.tnc.message.tnccs.message;


public interface TnccsMessageValueBuilder {

	public abstract TnccsMessageValue toValue();

	public abstract TnccsMessageValueBuilder clear();
}
