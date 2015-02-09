package de.hsbremen.tc.tnc.message.m.enums;

public enum TcgMProtocolEnum {

	M("IF-M");
	
	private String value;
	
	private TcgMProtocolEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
