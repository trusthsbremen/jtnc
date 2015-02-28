package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.ForwardingStatus;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueForwardingEnabledBuilderIetf implements
	PaAttributeValueForwardingEnabledBuilder {
	
	
	private long length;
	private PaAttributeForwardingStatusEnum status;      
	
	public PaAttributeValueForwardingEnabledBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.FWD_EN.length();
		this.status = PaAttributeForwardingStatusEnum.IETF_UNKNWON;
	}

	@Override
	public PaAttributeValueForwardingEnabledBuilder setStatus(long status) throws RuleException {
		
		ForwardingStatus.check(status);
		this.status = PaAttributeForwardingStatusEnum.fromNumber(status);
		
		return this;
	}

	@Override
	public PaAttributeValueForwardingEnabled toObject(){
		
		return new PaAttributeValueForwardingEnabled(this.length, this.status);
	}

	@Override
	public PaAttributeValueForwardingEnabledBuilder newInstance() {
		// TODO Auto-generated method stub
		return new PaAttributeValueForwardingEnabledBuilderIetf();
	}

}
