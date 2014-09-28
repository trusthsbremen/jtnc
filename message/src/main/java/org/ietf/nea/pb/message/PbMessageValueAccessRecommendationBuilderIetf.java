package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.validate.rules.AccessRecommendation;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public class PbMessageValueAccessRecommendationBuilderIetf implements
		PbMessageValueAccessRecommendationBuilder {
	
	
	private static final short RESERVED = 0;   // 16 bit(s) should be 0
    
    private PbMessageAccessRecommendationEnum recommendation;  //16 bit(s)
	
	public PbMessageValueAccessRecommendationBuilderIetf(){
		this.recommendation = PbMessageAccessRecommendationEnum.ALLOWED;
	}

	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder#setRecommendation(short)
	 */
	@Override
	public void setRecommendation(short recommendation) throws ValidationException {
		AccessRecommendation.check(recommendation);
		this.recommendation = PbMessageAccessRecommendationEnum.fromNumber(recommendation);
	}

	@Override
	public TnccsMessageValue toValue() throws ValidationException {
		
		return new PbMessageValueAccessRecommendation(RESERVED,this.recommendation);
	}

	@Override
	public TnccsMessageValueBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageValueAccessRecommendationBuilderIetf();
	}

}
