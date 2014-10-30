package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.validate.rules.ForwardingStatus;

public class PaAttributeValueForwardingEnabledBuilderIetf implements
	PaAttributeValueForwardingEnabledBuilder {
	
	
	private long length;
	private PaAttributeForwardingStatusEnum status;      
	
	public PaAttributeValueForwardingEnabledBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.FWD_EN.length();
		this.status = PaAttributeForwardingStatusEnum.IETF_UNKNWON;
	}

	@Override
	public PaAttributeValueForwardingEnabledBuilder setStatus(long status) throws RuleException {
		
		ForwardingStatus.check(status);
		this.status = PaAttributeForwardingStatusEnum.fromNumber(status);
		
		return this;
	}

	@Override
	public PaAttributeValueForwardingEnabled toValue(){
		
		return new PaAttributeValueForwardingEnabled(this.length, this.status);
	}

	@Override
	public PaAttributeValueForwardingEnabledBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeValueForwardingEnabledBuilderIetf();
	}

}
