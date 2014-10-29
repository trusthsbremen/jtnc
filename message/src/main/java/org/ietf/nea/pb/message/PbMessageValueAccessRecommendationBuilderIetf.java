package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.AccessRecommendation;

public class PbMessageValueAccessRecommendationBuilderIetf implements
		PbMessageValueAccessRecommendationBuilder {
    
	private long length;
    private PbMessageAccessRecommendationEnum recommendation;  //16 bit(s)
	
	public PbMessageValueAccessRecommendationBuilderIetf(){
		this.length = PbMessageTlvFixedLength.ACC_REC_VALUE.length();
		this.recommendation = PbMessageAccessRecommendationEnum.ALLOWED;
	}

	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder#setRecommendation(short)
	 */
	@Override
	public PbMessageValueAccessRecommendationBuilder setRecommendation(short recommendation) throws RuleException {
		
		AccessRecommendation.check(recommendation);
		this.recommendation = PbMessageAccessRecommendationEnum.fromNumber(recommendation);
		
		return this;
	}

	@Override
	public PbMessageValueAccessRecommendation toValue(){
		
		return new PbMessageValueAccessRecommendation(this.length, this.recommendation);
	}

	@Override
	public PbMessageValueAccessRecommendationBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageValueAccessRecommendationBuilderIetf();
	}

}
