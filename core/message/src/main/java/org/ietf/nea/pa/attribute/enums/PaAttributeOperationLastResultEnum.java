package org.ietf.nea.pa.attribute.enums;


/**
 * Reference IETF RFC 5792 section 4.2.5:
 * ------------------------------------
 * Value   Description
 * -----   -----------
 * 0       Unknown or other
 * 1       Successful use with no errors detected
 * 2       Successful use with one or more errors detected
 * 3       Unsuccessful use (e.g., aborted)
 *
 */

public enum PaAttributeOperationLastResultEnum{

    // IETF
    IETF_UNKNOWN            ((short)0),
    IETF_SUCCESSFUL         ((short)1),
    IETF_SUCCESSFUL_W_ERROR ((short)2),
    IETF_UNSUCCESSFUL   	((short)3);
 
    
    private short result;
    
    private PaAttributeOperationLastResultEnum(short result){
        this.result = result;
    }

    public short result(){
        return this.result;
    }
    
    public static PaAttributeOperationLastResultEnum fromNumber(short number){
    	
    	if(number == IETF_UNSUCCESSFUL.result){
    		return IETF_UNSUCCESSFUL;
    	}
    	
    	if(number == IETF_SUCCESSFUL_W_ERROR.result){
    		return IETF_SUCCESSFUL_W_ERROR;
    	}
    	
    	if(number == IETF_SUCCESSFUL.result){
    		return IETF_SUCCESSFUL;
    	}
    	
    	return IETF_UNKNOWN;
    }
}
