package org.ietf.nea.pb.message.enums;

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
public enum PbMessageErrorCodeEnum {
    // IETF
    IETF_UNEXPECTED_BATCH_TYPE          (0),
    IETF_INVALID_PARAMETER              (1),
    IETF_LOCAL                          (2),
    IETF_UNSUPPORTED_MANDATORY_MESSAGE  (3),
    IETF_UNSUPPORTED_VERSION            (4);
    
    private int code;
    
    private PbMessageErrorCodeEnum(int code){
        this.code = code;
    }
    
    public int code(){
        return this.code;
    }
    
    public static PbMessageErrorCodeEnum fromCode(int code){
    	
    	if(code == IETF_UNEXPECTED_BATCH_TYPE.code){
    		return IETF_UNEXPECTED_BATCH_TYPE;
    	}
    	
    	if(code == IETF_INVALID_PARAMETER.code){
    		return IETF_INVALID_PARAMETER;
    	}
    	
    	if(code == IETF_LOCAL.code){
    		return IETF_LOCAL;
    	}
    	
    	if(code == IETF_UNSUPPORTED_MANDATORY_MESSAGE.code){
    		return IETF_UNSUPPORTED_MANDATORY_MESSAGE;
    	}
    	
    	if(code == IETF_UNSUPPORTED_VERSION.code){
    		return IETF_UNSUPPORTED_VERSION;
    	}
    	
    	return null;
    }
}
