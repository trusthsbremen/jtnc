package de.hsbremen.tc.tnc.im.module;


public class SupportedMessageType implements Comparable<SupportedMessageType> {

    private final long vendorId;
    private final long type;
    
    public SupportedMessageType(final long vendorId, final long type) {
        
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

}
