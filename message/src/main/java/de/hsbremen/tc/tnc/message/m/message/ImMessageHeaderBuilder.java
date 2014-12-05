package de.hsbremen.tc.tnc.message.m.message;


public interface ImMessageHeaderBuilder {

	public abstract ImMessageHeader toMessageHeader();

	public abstract ImMessageHeaderBuilder clear();
}