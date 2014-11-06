package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.Arrays;

public class ImRawComponent extends AbstractImComponent{
    
    private final byte imFlags; //  8 bit(s)

    private final byte[] message;

    ImRawComponent(byte flags, final long subVendorId, final long subType,
			final long collectorId, final long validatorId, final byte[] message) {
    	super(subVendorId, subType, collectorId, validatorId);
    	
    	this.imFlags = flags;
		this.message = message;
	}
	/**
     * @return the flags
     */
    public byte getImFlags() {
        return this.imFlags;
    }
    
    /**
     * @return the message
     */
    public byte[] getMessage() {
        return Arrays.copyOf(message, message.length);
    }

}
