package de.hsbremen.tc.tnc.tnccs.batch;


public interface TnccsBatchHeaderBuilder {

	public abstract TnccsBatchHeader toBatchHeader();

	public abstract TnccsBatchHeaderBuilder clear();
}