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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AttributeReference [vendorId=" + this.vendorId + ", type="
				+ this.type + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.type ^ (this.type >>> 32));
		result = prime * result
				+ (int) (this.vendorId ^ (this.vendorId >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AttributeReference other = (AttributeReference) obj;
		if (this.type != other.type) {
			return false;
		}
		if (this.vendorId != other.vendorId) {
			return false;
		}
		return true;
	}
	
	
	
	
}
