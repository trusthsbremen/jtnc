package org.ietf.nea.pa.message;

import de.hsbremen.tc.tnc.m.message.ImMessageHeader;

public class PaMessageHeader implements ImMessageHeader{

	private final short version;	// 8 bit(s) 
	private final long identifier;  // 32 bit(s)

	PaMessageHeader(short version, long identifier) {
		this.version = version;
		this.identifier = identifier;
	}

	@Override
	public short getVersion() {
		return this.version;
	}

	@Override
	public long getIdentifier() {
		return this.identifier;
	}

}
