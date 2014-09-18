package de.hsbremen.tc.tnc.im.container;

import de.hsbremen.tc.tnc.IETFConstants;

public class SupportedMessageType implements Comparable<SupportedMessageType> {

    private final long vendorId;
    private final long type;

    
    
    public SupportedMessageType(final long vendorId, final long type) {
        if(vendorId >= IETFConstants.IETF_MAX_VENDOR_ID){
        	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
        }
        if(type > IETFConstants.IETF_MAX_TYPE){
        	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
        }
        
    	this.vendorId = vendorId;
        this.type = type;
    }

    /**
     * @return the vendorId
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * @return the type
     */
    public long getType() {
        return type;
    }

    /**
     * Sort ascending by vendor id and second by type.
     */
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
