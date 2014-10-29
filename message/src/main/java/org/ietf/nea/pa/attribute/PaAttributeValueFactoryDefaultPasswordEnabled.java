package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeFactoryDefaultPasswordStatusEnum;


public class PaAttributeValueFactoryDefaultPasswordEnabled extends AbstractPaAttributeValue {
	
	private final PaAttributeFactoryDefaultPasswordStatusEnum factoryDefaultPasswordSet; // 32 bit(s)
	
	PaAttributeValueFactoryDefaultPasswordEnabled(long length, PaAttributeFactoryDefaultPasswordStatusEnum status) {
		super(length);
		
		this.factoryDefaultPasswordSet = status;
	}

	/**
	 * @return the factoryDefaultPasswordSet
	 */
	public PaAttributeFactoryDefaultPasswordStatusEnum getFactoryDefaultPasswordStatus() {
		return this.factoryDefaultPasswordSet;
	}
}
