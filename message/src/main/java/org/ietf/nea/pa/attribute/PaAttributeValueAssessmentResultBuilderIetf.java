package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.validate.rules.AssessmentResult;

public class PaAttributeValueAssessmentResultBuilderIetf implements
		PaAttributeValueAssessmentResultBuilder {
	
	
	private long length;
	private PaAttributeAssessmentResultEnum result;      
	
	public PaAttributeValueAssessmentResultBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.ASS_RES.length();
		this.result = PaAttributeAssessmentResultEnum.COMPLIANT;
	}

	@Override
	public PaAttributeValueAssessmentResultBuilder setResult(long result) throws RuleException {
		
		AssessmentResult.check(result);
		this.result = PaAttributeAssessmentResultEnum.fromNumber(result);
		
		return this;
	}

	@Override
	public PaAttributeValueAssessmentResult toValue(){
		
		return new PaAttributeValueAssessmentResult(this.length, this.result);
	}

	@Override
	public PaAttributeValueAssessmentResultBuilder clear() {

		return new PaAttributeValueAssessmentResultBuilderIetf();
	}

}
