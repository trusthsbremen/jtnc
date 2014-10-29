package org.ietf.nea.pa.attribute.enums;

/**
 * Reference IETF RFC 5792 section 4.2.9:
 * ------------------------------------
 *    This 32-bit field MUST contain one of the following values;
 *
 *     Value   Description
 *     -----   -----------
 *     0      Posture Validator assessed the endpoint component to
 *            be compliant with policy.
 *
 *     1      Posture Validator assessed the endpoint component to
 *            be non-compliant with policy but the difference from
 *            compliant was minor.
 *
 *     2      Posture Validator assessed the endpoint component to
 *            be non-compliant with policy and the assessed
 *            difference was very significant.
 *            
 *     3      Posture Validator was unable to determine policy
 *            compliance of an endpoint component due to an error.
 *
 *     4      Posture Validator was unable to determine whether the
 *            assessed endpoint component was compliant with policy
 *            based on the attributes provided by the Posture
 *            Collector.
 *
 */
public enum PaAttributeAssessmentResultEnum {

    COMPLIANT               (0),
    MINOR_DIFFERENCES       (1),
    SIGNIFICANT_DIFFERENCES (2),
    ERROR                   (3),
    INSUFFICIANT_ATTRIBUTES (4);
    
    private long number;
    
    private PaAttributeAssessmentResultEnum(long number){
        this.number = number;
    }
    
    public long number(){
        return this.number;
    }
    
    public static PaAttributeAssessmentResultEnum fromNumber(long number){
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
