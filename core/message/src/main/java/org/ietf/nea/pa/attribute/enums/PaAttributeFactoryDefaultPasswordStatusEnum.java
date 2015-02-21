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

public enum PaAttributeFactoryDefaultPasswordStatusEnum{

    // IETF
    IETF_NOT_SET         ((byte)0),
    IETF_SET         	 ((byte)1);

    private long status;
    
    private PaAttributeFactoryDefaultPasswordStatusEnum(long status){
        this.status = status;
    }

    public long status(){
        return this.status;
    }
    
    public static PaAttributeFactoryDefaultPasswordStatusEnum fromNumber(long number){
    	if(number == IETF_NOT_SET.status){
    		return IETF_NOT_SET;
    	}
    	if(number == IETF_SET.status){
    		return IETF_SET;
    	}

    	return null;
    }
}
