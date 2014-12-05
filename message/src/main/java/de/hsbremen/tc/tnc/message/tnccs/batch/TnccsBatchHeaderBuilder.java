package de.hsbremen.tc.tnc.message.tnccs.batch;


public interface TnccsBatchHeaderBuilder {

	public abstract TnccsBatchHeader toBatchHeader();

	public abstract TnccsBatchHeaderBuilder clear();
}