package org.ietf.nea.pa.attribute.util;

public class MessageHeaderCopy {

	private final short version;	// 8 bit(s) 
	private final byte[] reserved;
	private final long identifier;  // 32 bit(s)

	public MessageHeaderCopy(short version, byte[] reserved, long identifier) {
		this.version = version;
		this.reserved = reserved;
		this.identifier = identifier;
	}

	public short getVersion() {
		return this.version;
	}

	public long getIdentifier() {
		return this.identifier;
	}

	/**
	 * @return the reserved
	 */
	public byte[] getReserved() {
		return this.reserved;
	}
	
	
}
