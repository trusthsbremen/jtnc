package de.hsbremen.tc.tnc.tnccs.message;

public interface TnccsMessageHeaderBuilder {
	
	public abstract TnccsMessageHeader toMessageHeader();

	public abstract TnccsMessageHeaderBuilder clear();
}
