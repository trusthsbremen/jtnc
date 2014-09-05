package de.hsbremen.tc.tnc.imc.util;

public class ImcSupportedMessageType implements Comparable<ImcSupportedMessageType> {

    private long vendorId;
    private long type;

    
    
    public ImcSupportedMessageType(long vendorId, long type) {
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
    public int compareTo(ImcSupportedMessageType o) {
        if (this.getVendorId() < o.getVendorId()) return -1;
        if (this.getVendorId() > o.getVendorId()) return 1;
        if (this.getVendorId() == o.getVendorId()) {
            if (this.getType() < o.getType()) return -1;
            if (this.getType() > o.getType()) return 1;
        }
        return 0;
    }

}
