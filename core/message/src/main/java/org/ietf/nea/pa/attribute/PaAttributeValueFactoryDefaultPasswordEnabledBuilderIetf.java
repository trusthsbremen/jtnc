package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeFactoryDefaultPasswordStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.FactoryDefaultPasswordStatus;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf implements
	PaAttributeValueFactoryDefaultPasswordEnabledBuilder {
	
	
	private long length;
	private PaAttributeFactoryDefaultPasswordStatusEnum status;      
	
	public PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.FAC_PW.length();
		this.status = PaAttributeFactoryDefaultPasswordStatusEnum.NOT_SET;
	}

	@Override
	public PaAttributeValueFactoryDefaultPasswordEnabledBuilder setStatus(long status) throws RuleException {
		
		FactoryDefaultPasswordStatus.check(status);
		this.status = PaAttributeFactoryDefaultPasswordStatusEnum.fromId(status);
		
		return this;
	}

	@Override
	public PaAttributeValueFactoryDefaultPasswordEnabled toObject(){
		
		return new PaAttributeValueFactoryDefaultPasswordEnabled(this.length, this.status);
	}

	@Override
	public PaAttributeValueFactoryDefaultPasswordEnabledBuilder newInstance() {
		// TODO Auto-generated method stub
		return new PaAttributeValueFactoryDefaultPasswordEnabledBuilderIetf();
	}

}
