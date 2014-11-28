package de.hsbremen.tc.tnc.report.enums;

public enum ImvActionRecommendationEnum {

	/**
    * IMV recommends allowing access.
    */
    TNC_IMV_ACTION_RECOMMENDATION_ALLOW (0),
    
    /**
     * IMV recommends no access.
     */
    TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS (1),
    
    /**
     * IMV recommends limited access. This access may be
     * expanded after remediation.
     */
    TNC_IMV_ACTION_RECOMMENDATION_ISOLATE (2),
    
    /**
     * IMV does not have a recommendation.
     */
    TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION (3);
	
    private final long number;
    
    private ImvActionRecommendationEnum(long number){
    	this.number = number;
    }

	/**
	 * @return the number
	 */
	public long number() {
		return this.number;
	}
    
	
	public static ImvActionRecommendationEnum fromNumber(long number){

		if (number == TNC_IMV_ACTION_RECOMMENDATION_ALLOW.number) {
			return TNC_IMV_ACTION_RECOMMENDATION_ALLOW;
		}

		if (number == TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS.number) {
			return TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS;
		}

		if (number == TNC_IMV_ACTION_RECOMMENDATION_ISOLATE.number) {
			return TNC_IMV_ACTION_RECOMMENDATION_ISOLATE;
		}

		if (number == TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION.number) {
			return TNC_IMV_ACTION_RECOMMENDATION_NO_RECOMMENDATION;
		}

		    return null;
	}
    
}
