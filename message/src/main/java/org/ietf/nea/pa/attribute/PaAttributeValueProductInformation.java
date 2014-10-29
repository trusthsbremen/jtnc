package org.ietf.nea.pa.attribute;


public class PaAttributeValueProductInformation extends AbstractPaAttributeValue {
	
	private final long vendorId; // 24 bit(s)
	private final int productId; // 16 bit(s)
	private final String name; // variable length
	
	PaAttributeValueProductInformation(long length, long vendorId, int productId, String name) {
		super(length);
		
		this.vendorId = vendorId;
		this.productId = productId;
		this.name = name;
	}

	/**
	 * @return the vendorId
	 */
	public long getVendorId() {
		return this.vendorId;
	}

	/**
	 * @return the productId
	 */
	public int getProductId() {
		return this.productId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	

	
}
