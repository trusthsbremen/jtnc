package de.hsbremen.tc.tnc.message.tnccs.enums;

public enum TcgTnccsProtocolEnum {

	TNCCS("IF-TNCCS"),
	TNCCS_SOH("IF-TNCCS-SOH");
	
	private String value;
	
	private TcgTnccsProtocolEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
