package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.AssessmentResult;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueAssessmentResultBuilderIetf implements
		PaAttributeValueAssessmentResultBuilder {
	
	
	private long length;
	private PaAttributeAssessmentResultEnum result;      
	
	public PaAttributeValueAssessmentResultBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.ASS_RES.length();
		this.result = PaAttributeAssessmentResultEnum.COMPLIANT;
	}

	@Override
	public PaAttributeValueAssessmentResultBuilder setResult(long result) throws RuleException {
		
		AssessmentResult.check(result);
		this.result = PaAttributeAssessmentResultEnum.fromNumber(result);
		
		return this;
	}

	@Override
	public PaAttributeValueAssessmentResult toObject(){
		
		return new PaAttributeValueAssessmentResult(this.length, this.result);
	}

	@Override
	public PaAttributeValueAssessmentResultBuilder newInstance() {

		return new PaAttributeValueAssessmentResultBuilderIetf();
	}

}
