package org.ietf.nea.pt.message.enums;

/**
 * Enum which holds the fixed length of different messages. 
 * The length values are not cumulated.
 * 
 * @author Carl-Heinz Genzel
 *
 */
public enum PtTlsMessageTlvFixedLengthEnum {

	MESSAGE((byte)16),
	VER_REQ((byte)4),
	VER_RES((byte)4),
	SASL_SEL((byte)1),
	SASL_RLT((byte)2),
	ERR_VALUE((byte)8);
	
	private byte length;
	
	private PtTlsMessageTlvFixedLengthEnum(byte length){
		this.length = length;
	}

	public byte length(){
		return this.length;
	}
}
