package de.hsbremen.tc.tnc.message.tnccs.enums;

public enum TcgTnccsVersionEnum {

	V1("1.0"),
	V1_1("1.1"),
	V2("2.0");
	
	private String value;
	
	private TcgTnccsVersionEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
