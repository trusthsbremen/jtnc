package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.AssessmentResult;

public class PbMessageValueAssessmentResultBuilderIetf implements
		PbMessageValueAssessmentResultBuilder {
	
	
	private long length;
	private PbMessageAssessmentResultEnum result;      
	
	public PbMessageValueAssessmentResultBuilderIetf(){
		this.length = PbMessageTlvFixedLengthEnum.ASS_RES_VALUE.length();
		this.result = PbMessageAssessmentResultEnum.COMPLIANT;
	}

	@Override
	public PbMessageValueAssessmentResultBuilder setResult(long result) throws RuleException {
		
		AssessmentResult.check(result);
		this.result = PbMessageAssessmentResultEnum.fromNumber(result);
		
		return this;
	}

	@Override
	public PbMessageValueAssessmentResult toObject(){
		
		return new PbMessageValueAssessmentResult(this.length, this.result);
	}

	@Override
	public PbMessageValueAssessmentResultBuilder newInstance() {
		return new PbMessageValueAssessmentResultBuilderIetf();
	}

}
