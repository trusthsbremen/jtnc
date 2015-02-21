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

    ALLOWED     (1),
    DENIED      (2),
    QURANTINED  (3);
   
    private int number;
    
    private PbMessageAccessRecommendationEnum(int number){
        this.number = number;
    }
    
    public int number(){
        return this.number;
    }
    
    public static PbMessageAccessRecommendationEnum fromNumber(int number){
    	
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
