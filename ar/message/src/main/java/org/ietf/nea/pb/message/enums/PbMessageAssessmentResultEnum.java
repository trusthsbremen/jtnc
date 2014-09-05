package org.ietf.nea.pb.message.enums;

/**
 * Reference IETF RFC 5793 section 4.6:
 * ------------------------------------
 *    This 32-bit field MUST contain one of the following values
 *
 *    Value   Description
 *    -----   -----------
 *    0       Posture Broker Server assessed the endpoint to be
 *            compliant with policy.
 *
 *    1       Posture Broker Server assessed the endpoint to be non-
 *            compliant with policy but the difference from compliance
 *            was minor.
 *
 *    2       Posture Broker Server assessed the endpoint to be non-
 *            compliant with policy and the assessed difference from
 *            compliance was very significant.
 *            
 *    3       Posture Broker Server was unable to determine policy
 *            compliance due to an error.
 *
 *    4       Posture Broker Server was unable to determine whether the
 *            assessed endpoint is compliant with policy based on the
 *            attributes provided by endpoint.
 *
 */
public enum PbMessageAssessmentResultEnum {

    COMPLIANT               (0),
    MINOR_DIFFERENCES       (1),
    SIGNIFICANT_DIFFERENCES (2),
    ERROR                   (3),
    INSUFFICIANT_ATTRIBUTES (4);
    
    private long number;
    
    private PbMessageAssessmentResultEnum(long number){
        this.number = number;
    }
    
    public long number(){
        return this.number;
    }
    
    public static PbMessageAssessmentResultEnum fromNumber(long number){
    	if(number == COMPLIANT.number){
    		return COMPLIANT;
    	}
    	if(number == MINOR_DIFFERENCES.number){
    		return MINOR_DIFFERENCES;
    	}
    	
    	if(number == SIGNIFICANT_DIFFERENCES.number){
    		return SIGNIFICANT_DIFFERENCES;
    	}
    	
    	if(number == ERROR.number){
    		return ERROR;
    	}
    	
    	if(number == INSUFFICIANT_ATTRIBUTES.number){
    		return INSUFFICIANT_ATTRIBUTES;
    	}
    	
    	return null;
    }
}
