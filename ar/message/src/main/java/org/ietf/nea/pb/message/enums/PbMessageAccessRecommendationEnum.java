package org.ietf.nea.pb.message.enums;

/**
 * Reference IETF RFC 5793 section 4.7:
 * ------------------------------------
 *    This field MUST have one of these three values: 
 *    
 *    Value     Description
 *    -----     ------------
 *    1         Access Allowed (full access)
 *    2         Access Denied (no access)
 *    3         Quarantined (partial access)
 *
 */
public enum PbMessageAccessRecommendationEnum {

    ALLOWED     ((short)1),
    DENIED      ((short)2),
    QURANTINED  ((short)3);
   
    private short number;
    
    private PbMessageAccessRecommendationEnum(short number){
        this.number = number;
    }
    
    public short number(){
        return this.number;
    }
    
    public static PbMessageAccessRecommendationEnum fromNumber(short number){
    	
    	if(number == ALLOWED.number){
			return ALLOWED;
		}
    	
    	if(number == QURANTINED.number){
			return QURANTINED;
		}
    	
    	if(number == DENIED.number){
			return DENIED;
		}

    	return null;
    }
}
