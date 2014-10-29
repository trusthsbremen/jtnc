package org.ietf.nea.pa.attribute.enums;



public enum PaAttributePortFilterStatus {
    ALLOWED  (false),
    BLOCKED  (true);
    
    private final boolean blocked;
   
    private PaAttributePortFilterStatus(boolean blocked){
        this.blocked = blocked;
    }
    
	public boolean blocked(){
        return this.blocked;
    }
	
	public byte toBlockedBit(){
		return (byte) (this.blocked ? 1 : 0); 
	}
    
    public static PaAttributePortFilterStatus fromBlockedBit(byte directionality){
    	
    	if(directionality == 0){
    		return ALLOWED;
    	}
    	
    	if(directionality == 1){
    		return BLOCKED;
    	}
    	
    	return null;
    }
}
