package org.ietf.nea.pa.attribute.enums;


/**
 * Reference IETF RFC 5792 section 4.2.5:
 * ------------------------------------
 * Value   Description
 * -----   -----------
 * 0       Unknown or other
 * 1       Not installed
 * 2       Installed but not operational
 * 3       Operational
 */

public enum PaAttributeOperationStatusEnum{

    // IETF
    IETF_UNKNOWN            		((short)0),
    IETF_NOT_INSTALLED              ((short)1),
    IETF_INSTALLED_NOT_OPERATIONAL  ((short)2),
    IETF_OPERATIONAL   				((short)3);
 
    
    private short status;
    
    private PaAttributeOperationStatusEnum(short status){
        this.status = status;
    }

    public short status(){
        return this.status;
    }
    
    public static PaAttributeOperationStatusEnum fromNumber(short number){
    	
    	if(number == IETF_OPERATIONAL.status){
    		return IETF_OPERATIONAL;
    	}
    	
    	if(number == IETF_INSTALLED_NOT_OPERATIONAL.status){
    		return IETF_INSTALLED_NOT_OPERATIONAL;
    	}
    	
    	if(number == IETF_NOT_INSTALLED.status){
    		return IETF_NOT_INSTALLED;
    	}
    	
    	return IETF_UNKNOWN;
    }
    
}
