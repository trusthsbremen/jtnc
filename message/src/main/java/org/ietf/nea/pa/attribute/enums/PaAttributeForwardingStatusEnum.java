package org.ietf.nea.pa.attribute.enums;


/**
 * Reference IETF RFC 5792 section 4.2.11:
 * ------------------------------------
 * Value   Description
 * -----   -----------
 * 0       Disabled - Endpoint is not forwarding traffic.
 *
 * 1       Enabled -  Endpoint is forwarding traffic.
 *
 * 2       Unknown -  Unable to determine whether endpoint is
 *                    forwarding traffic

 */

public enum PaAttributeForwardingStatusEnum{

    // IETF
    IETF_DISABLED         ((byte)0),
    IETF_ENABLED          ((byte)1),
    IETF_UNKNWON		  ((byte)2);

    private long status;
    
    private PaAttributeForwardingStatusEnum(long status){
        this.status = status;
    }

    public long status(){
        return this.status;
    }
    
    public static PaAttributeForwardingStatusEnum fromNumber(long number){
    	if(number == IETF_DISABLED.status){
    		return IETF_DISABLED;
    	}
    	if(number == IETF_ENABLED.status){
    		return IETF_ENABLED;
    	}
    	
    	if(number == IETF_UNKNWON.status){
    		return IETF_UNKNWON;
    	}
    	
    	return null;
    }
}
