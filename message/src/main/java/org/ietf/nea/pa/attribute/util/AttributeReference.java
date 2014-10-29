package org.ietf.nea.pa.attribute.util;

public class AttributeReference {

	private final long vendorId; // 24 bit(s)
	private final long type;     // 32 bit(s)

	public AttributeReference(long vendorId, long type) {
		this.vendorId = vendorId;
		this.type = type;
	}

	/**
	 * @return the vendorId
	 */
	public long getVendorId() {
		return this.vendorId;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return this.type;
	}
	
	
	
}
