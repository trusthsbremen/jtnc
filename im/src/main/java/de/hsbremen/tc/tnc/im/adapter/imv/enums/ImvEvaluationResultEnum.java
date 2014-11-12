package de.hsbremen.tc.tnc.im.adapter.imv.enums;


public enum ImvEvaluationResultEnum {

	/**
     * AR complies with policy.
     */
    
    TNC_IMV_EVALUATION_RESULT_COMPLIANT (0),
    
    /**
     * AR is not compliant with policy. Non-compliance is minor.
     */
    
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR (1),
    
    /**
     * AR is not compliant with policy. Non-compliance is major.
     */
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR (2),
    
    /**
     * IMV is unable to determine policy compliance due to error.
     */
    
    TNC_IMV_EVALUATION_RESULT_ERROR (3),
    
    /**
     * IMV does not know whether AR complies with policy.
     */
    TNC_IMV_EVALUATION_RESULT_DONT_KNOW (4);
    
    private final long code;
    
    private ImvEvaluationResultEnum(long code){
    	this.code = code;
    }

	/**
	 * @return the number
	 */
	public long code() {
		return this.code;
	}
	
	public static ImvEvaluationResultEnum fromResult(long result){
	
		if (result == TNC_IMV_EVALUATION_RESULT_COMPLIANT.code) {
			return TNC_IMV_EVALUATION_RESULT_COMPLIANT;
		}

		if (result == TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR.code) {
			return TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR;
		}

		if (result == TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR.code) {
			return TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR;
		}

		if (result == TNC_IMV_EVALUATION_RESULT_ERROR.code) {
			return TNC_IMV_EVALUATION_RESULT_ERROR;
		}

		if (result == TNC_IMV_EVALUATION_RESULT_DONT_KNOW.code) {
			return TNC_IMV_EVALUATION_RESULT_DONT_KNOW;
		}
	    
	    return null;
	}
}
