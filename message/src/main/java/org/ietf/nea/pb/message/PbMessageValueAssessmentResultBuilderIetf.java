package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.AssessmentResult;

public class PbMessageValueAssessmentResultBuilderIetf implements
		PbMessageValueAssessmentResultBuilder {
	
	
	private long length;
	private PbMessageAssessmentResultEnum result;      
	
	public PbMessageValueAssessmentResultBuilderIetf(){
		this.length = PbMessageTlvFixedLength.ASS_RES_VALUE.length();
		this.result = PbMessageAssessmentResultEnum.COMPLIANT;
	}

	@Override
	public PbMessageValueAssessmentResultBuilder setResult(long result) throws RuleException {
		
		AssessmentResult.check(result);
		this.result = PbMessageAssessmentResultEnum.fromNumber(result);
		
		return this;
	}

	@Override
	public PbMessageValueAssessmentResult toValue(){
		
		return new PbMessageValueAssessmentResult(this.length, this.result);
	}

	@Override
	public PbMessageValueAssessmentResultBuilder clear() {
		return new PbMessageValueAssessmentResultBuilderIetf();
	}

}
