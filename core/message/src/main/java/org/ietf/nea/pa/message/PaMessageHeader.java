package org.ietf.nea.pa.message;

import de.hsbremen.tc.tnc.message.m.message.ImMessageHeader;

public class PaMessageHeader implements ImMessageHeader{

	private final short version;	// 8 bit(s) 
	private final long identifier;  // 32 bit(s)
	private long length;      // not official part   
	
	PaMessageHeader(short version, long identifier, long length) {
		this.version = version;
		this.identifier = identifier;
		this.length = length;
	}

	@Override
	public short getVersion() {
		return this.version;
	}

	@Override
	public long getIdentifier() {
		return this.identifier;
	}

	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
	

}
