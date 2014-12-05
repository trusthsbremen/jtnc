package de.hsbremen.tc.tnc.message.tnccs.message;

public interface TnccsMessageHeaderBuilder {
	
	public abstract TnccsMessageHeader toMessageHeader();

	public abstract TnccsMessageHeaderBuilder clear();
}
