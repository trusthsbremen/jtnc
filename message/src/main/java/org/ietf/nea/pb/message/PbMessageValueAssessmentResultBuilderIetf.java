package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.validate.rules.AssessmentResult;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueAssessmentResultBuilderIetf implements
		PbMessageValueAssessmentResultBuilder {
	
	
	private PbMessageAssessmentResultEnum result;      
	
	public PbMessageValueAssessmentResultBuilderIetf(){
		this.result = PbMessageAssessmentResultEnum.COMPLIANT;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilder#setResult(long)
	 */
	@Override
	public void setResult(long result) throws ValidationException {
		AssessmentResult.check(result);
		this.result = PbMessageAssessmentResultEnum.fromNumber(result);
	}

	@Override
	public TnccsMessageValue toValue() throws ValidationException {
		
		return new PbMessageValueAssessmentResult(this.result);
	}

	@Override
	public TnccsMessageValueBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageValueAssessmentResultBuilderIetf();
	}

}
