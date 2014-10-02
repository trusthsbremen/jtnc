package org.ietf.nea.pb.message.enums;

public enum PbMessageTlvFixedLength {

	BATCH((byte)8),
	MESSAGE((byte)12),
	ACC_REC_VALUE((byte)4),
	ASS_RES_VALUE((byte)4),
	ERR_VALUE((byte)8),
	IM_VALUE((byte)12),
	REA_STR_VALUE((byte)5),
	REM_PAR_VALUE((byte)8),
	REM_STR_SUB_VALUE((byte)5);
	
	private byte length;
	
	private PbMessageTlvFixedLength(byte length){
		this.length = length;
	}

	public byte length(){
		return this.length;
	}
}