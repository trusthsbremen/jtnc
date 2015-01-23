package de.hsbremen.tc.tnc.report.enums;


public enum ImvEvaluationResultEnum {

	/**
     * AR complies with policy.
     */
    
    TNC_IMV_EVALUATION_RESULT_COMPLIANT (0L),
    
    /**
     * AR is not compliant with policy. Non-compliance is minor.
     */
    
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MINOR (1L),
    
    /**
     * AR is not compliant with policy. Non-compliance is major.
     */
    TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR (2L),
    
    /**
     * IMV is unable to determine policy compliance due to error.
     */
    
    TNC_IMV_EVALUATION_RESULT_ERROR (3L),
    
    /**
     * IMV does not know whether AR complies with policy.
     */
    TNC_IMV_EVALUATION_RESULT_DONT_KNOW (4L);
    
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
