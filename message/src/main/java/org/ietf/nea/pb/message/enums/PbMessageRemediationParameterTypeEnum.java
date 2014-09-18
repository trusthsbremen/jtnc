package org.ietf.nea.pb.message.enums;

/**
 * Reference IETF RFC 5793 section 4.8.1:
 * --------------------------------------
 * VendorId     Type   Definition
 * --------     -----  ----------
 * 0            1      URI as remediation parameter 
 * 0            2      String as remediation parameter
 * 
 */

public enum PbMessageRemediationParameterTypeEnum {

    // IETF
    IETF_URI    (1),
    IETF_STRING (2);
    
    private long type;
    
    private PbMessageRemediationParameterTypeEnum(long type){
        
        this.type = type;
    }
    
    public long type(){
        return this.type;
    }
    
    public static PbMessageRemediationParameterTypeEnum fromType(long type){
    	if(type == IETF_URI.type){
    		return IETF_URI;
    	}
    	
    	if(type == IETF_STRING.type){
    		return IETF_STRING;
    	}
    	
    	return null;
    }
    
}
