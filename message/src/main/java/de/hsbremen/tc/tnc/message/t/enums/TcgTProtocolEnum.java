package de.hsbremen.tc.tnc.message.t.enums;

public enum TcgTProtocolEnum {
	TEAP("IF-T for Tunneled EAP"),
	TLS("IF-T for TLS"),
	PEAP("PEAP");
	
	private String value;
	
	private TcgTProtocolEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
