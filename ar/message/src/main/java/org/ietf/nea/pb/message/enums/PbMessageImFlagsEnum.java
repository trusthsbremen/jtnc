package org.ietf.nea.pb.message.enums;

public enum PbMessageImFlagsEnum {

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

	private PbMessageImFlagsEnum(byte b) {
		this.bit = b;
	}

	public final byte bit() {
		return bit;
	}
	
	public static PbMessageImFlagsEnum fromBit(byte bit){
		if(bit == RESERVED1.bit){
			return RESERVED1;
		}
		
		if(bit == RESERVED2.bit){
			return RESERVED2;
		}
		
		if(bit == RESERVED3.bit){
			return RESERVED3;
		}
		
		if(bit == RESERVED4.bit){
			return RESERVED4;
		}
		
		if(bit == RESERVED5.bit){
			return RESERVED5;
		}
		
		if(bit == RESERVED6.bit){
			return RESERVED6;
		}
		
		if(bit == RESERVED7.bit){
			return RESERVED7;
		}
		
		if(bit == EXCL.bit){
			return EXCL;
		}
		
		return null;
	}
}
