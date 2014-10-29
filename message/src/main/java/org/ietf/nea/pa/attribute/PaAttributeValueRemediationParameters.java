package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueRemediationParameter;

public class PaAttributeValueRemediationParameters extends AbstractPaAttributeValue {

	 private final long rpVendorId;         // 24 bit(s)
	 private final long rpType;             // 32 bit(s)
	    
	 private final AbstractPaAttributeValueRemediationParameter parameter;
	    

	    
	    
	 PaAttributeValueRemediationParameters(final long length,
			final long rpVendorId, final long rpType,
			final AbstractPaAttributeValueRemediationParameter parameter) {
			super(length);
			this.rpVendorId = rpVendorId;
			this.rpType = rpType;
			this.parameter = parameter;
	 }


	/**
	 * @return the rpVendorId
	 */
	public long getRpVendorId() {
		return this.rpVendorId;
	}


	/**
	 * @return the rpType
	 */
	public long getRpType() {
		return this.rpType;
	}

	public AbstractPaAttributeValueRemediationParameter getParameter(){
		return this.parameter;
	}
}
