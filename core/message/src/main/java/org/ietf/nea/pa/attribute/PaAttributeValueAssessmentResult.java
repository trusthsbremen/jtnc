package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;

public class PaAttributeValueAssessmentResult extends AbstractPaAttributeValue {
	
	private final PaAttributeAssessmentResultEnum result; // 32 bit(s)
	
	PaAttributeValueAssessmentResult(long length, PaAttributeAssessmentResultEnum result) {
		super(length);
		
		this.result = result;
	}

	/**
	 * @return the result
	 */
	public PaAttributeAssessmentResultEnum getResult() {
		return this.result;
	}

	

	
}
