package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;

public class PaAttributeValueError extends AbstractPaAttributeValue {
	
	private final long errorVendorId;                                           // 24 bit(s)
    private final long errorCode;  												// 32 bit(s)	
	
    private final AbstractPaAttributeValueErrorInformation errorInformation;
    
	PaAttributeValueError(final long length, final long errorVendorId, final long errorCode, final AbstractPaAttributeValueErrorInformation errorInformation) {
		super(length);
		this.errorVendorId = errorVendorId;
		this.errorCode = errorCode;
		this.errorInformation = errorInformation;
	}

	/**
	 * @return the errorVendorId
	 */
	public long getErrorVendorId() {
		return this.errorVendorId;
	}

	/**
	 * @return the errorCode
	 */
	public long getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @return the errorInformation
	 */
	public AbstractPaAttributeValueErrorInformation getErrorInformation() {
		return this.errorInformation;
	}

}
