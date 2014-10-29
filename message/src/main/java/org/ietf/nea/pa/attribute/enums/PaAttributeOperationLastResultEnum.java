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
    IETF_UNKNOWN            ((byte)0),
    IETF_SUCCESSFUL         ((byte)1),
    IETF_SUCCESSFUL_W_ERROR ((byte)2),
    IETF_UNSUCCESSFUL   	((byte)3);
 
    
    private byte result;
    
    private PaAttributeOperationLastResultEnum(byte result){
        this.result = result;
    }

    public byte result(){
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
