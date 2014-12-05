package de.hsbremen.tc.tnc.message.t.enums;

public enum TcgTVersionEnum {
	V1("1.0"),
	V1_1("1.1");
	
	private String value;
	
	private TcgTVersionEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
