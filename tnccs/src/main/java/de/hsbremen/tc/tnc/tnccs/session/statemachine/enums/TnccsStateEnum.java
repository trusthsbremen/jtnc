package de.hsbremen.tc.tnc.tnccs.session.statemachine.enums;

public enum TnccsStateEnum {

	INIT("Init"),
	SERVER_WORKING("Server Working"),
	CLIENT_WORKING("Client Working"),
	DECIDED("Decided"),
	END("End"),
	ERROR("Error"),
	RETRY("Retry");
	
	private String value;
	
	private TnccsStateEnum(String value){
		this.value = value;
	}
	
	public String value(){
		return this.value;
	}
	
	
	
}
