package de.hsbremen.tc.tnc.report;


public class SupportedMessageType implements Comparable<SupportedMessageType> {

    private final long vendorId;
    private final long type;
    
    SupportedMessageType(final long vendorId, final long type) {
        
    	this.vendorId = vendorId;
        this.type = type;
    }

	public long getVendorId() {
        return vendorId;
    }

	public long getType() {
        return type;
    }

	@Override
    public int compareTo(SupportedMessageType o) {
        if (this.getVendorId() < o.getVendorId()) return -1;
        if (this.getVendorId() > o.getVendorId()) return 1;
        if (this.getVendorId() == o.getVendorId()) {
            if (this.getType() < o.getType()) return -1;
            if (this.getType() > o.getType()) return 1;
        }
        return 0;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SupportedMessageType [vendorId=" + this.vendorId + ", type="
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
		SupportedMessageType other = (SupportedMessageType) obj;
		if (this.type != other.type) {
			return false;
		}
		if (this.vendorId != other.vendorId) {
			return false;
		}
		return true;
	}
	
	

}
