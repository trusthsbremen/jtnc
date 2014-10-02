package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;
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

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilder#setResult(long)
	 */
	@Override
	public PbMessageValueAssessmentResultBuilder setResult(long result) throws RuleException {
		
		AssessmentResult.check(result);
		this.result = PbMessageAssessmentResultEnum.fromNumber(result);
		
		return this;
	}

	@Override
	public PbMessageValueAssessmentResult toValue() throws RuleException {
		
		return new PbMessageValueAssessmentResult(this.length, this.result);
	}

	@Override
	public PbMessageValueAssessmentResultBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageValueAssessmentResultBuilderIetf();
	}

}
