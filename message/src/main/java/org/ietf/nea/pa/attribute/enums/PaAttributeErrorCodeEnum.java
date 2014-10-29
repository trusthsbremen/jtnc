package org.ietf.nea.pa.attribute.enums;

/**
 * Reference IETF RFC 5793 section 4.9.1:
 * --------------------------------------
 *  Error Code  Definition
 * ----------  ----------
 * 0           Unexpected Batch Type.  Error Parameters are empty.
 *
 * 1           Invalid Parameter.  Error Parameters has offset where
 *             invalid value was found.
 *
 * 2           Local Error.  Error Parameters are empty.
 * 
 * 3           Unsupported Mandatory Message.  Error Parameters has
 *             offset of offending PB-TNC Message
 *
 * 4           Version Not Supported.  Error Parameters has information
 *             about which versions are supported.
 */
public enum PaAttributeErrorCodeEnum {
    // IETF
    IETF_RESERVED          				(0),
    IETF_INVALID_PARAMETER              (1),
    IETF_UNSUPPORTED_VERSION            (2),
    IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE (3);
    
    private long code;
    
    private PaAttributeErrorCodeEnum(long code){
        this.code = code;
    }
    
    public long code(){
        return this.code;
    }
    
    public static PaAttributeErrorCodeEnum fromCode(long code){

    	if(code == IETF_RESERVED.code){
    		return IETF_RESERVED;
    	}
    	
    	if(code == IETF_INVALID_PARAMETER.code){
    		return IETF_INVALID_PARAMETER;
    	}
    	
    	if(code ==  IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code){
    		return  IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE;
    	}
    	
    	if(code == IETF_UNSUPPORTED_VERSION.code){
    		return IETF_UNSUPPORTED_VERSION;
    	}
    	
    	return null;
    }
}
