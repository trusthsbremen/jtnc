package de.hsbremen.tc.tnc.im.adapter.data.enums;

public enum ImComponentFlagsEnum {

	// sort by bits from least significant to most significant.
	// do not use these, because they are not specified yet
	RESERVED1((byte) 0x01),
	RESERVED2((byte) 0x02),
	RESERVED3((byte) 0x04),
	RESERVED4((byte) 0x08),
	RESERVED5((byte) 0x10),
	RESERVED6((byte) 0x20),
	RESERVED7((byte) 0x40),
	// only use this one
	EXCL((byte) 0x80);

	private final byte bit;

	private ImComponentFlagsEnum(byte b) {
		this.bit = b;
	}

	public final byte bit() {
		return bit;
	}
}
