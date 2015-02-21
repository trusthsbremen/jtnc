package de.hsbremen.tc.tnc.message.m.enums;

public enum TcgMVersionEnum {

	V1("1.0");
	
	private String value;
	
	private TcgMVersionEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
}
