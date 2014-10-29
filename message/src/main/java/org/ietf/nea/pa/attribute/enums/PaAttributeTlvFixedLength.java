package org.ietf.nea.pa.attribute.enums;

public enum PaAttributeTlvFixedLength {

	MESSAGE((byte) 8),
	ATTRIBUTE((byte) 12),
	ATT_REQ((byte) 8),
	PRO_INF((byte) 5),
	NUM_VER((byte) 16),
	STR_VER((byte) 3),
	OP_STAT((byte) 24),
	PORT_FT((byte) 4),
	INS_PKG((byte) 4), 
	ASS_RES((byte) 4), 
	FAC_PW((byte) 4), 
	FWD_EN((byte) 4), 
	REM_PAR((byte) 8),
	REM_PAR_STR ((byte) 5),
	ERR_INF((byte) 8);
	
	private byte length;
	
	private PaAttributeTlvFixedLength(byte length){
		this.length = length;
	}

	public byte length(){
		return this.length;
	}
}
