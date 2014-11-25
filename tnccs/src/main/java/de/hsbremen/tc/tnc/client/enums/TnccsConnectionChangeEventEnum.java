package de.hsbremen.tc.tnc.client.enums;

public enum TnccsConnectionChangeEventEnum {

	HANDSHAKE (100), 
	DELETE (200);
	
	private final int id;
	
	private TnccsConnectionChangeEventEnum(int id){
		this.id = id;
	}
	
	public int id(){
		return this.id;
	}
}
