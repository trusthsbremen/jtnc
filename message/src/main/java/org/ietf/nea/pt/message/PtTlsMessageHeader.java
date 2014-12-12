package org.ietf.nea.pt.message;

import de.hsbremen.tc.tnc.message.t.message.TransportMessageHeader;


public class PtTlsMessageHeader implements TransportMessageHeader{

	    private final long vendorId;                                // 24 bit(s)
	    private final long type;                                    // 32 bit(s)
	    private final long length;									// 32 bit(s) min value is 16 for the 16 bytes in this header 
	    private final long identifier;								// 32 bit(s)
	    
	    public PtTlsMessageHeader(final long vendorId, final long type, final long length, final long identifier) {
	    	this.vendorId = vendorId;
			this.type = type;
			this.length = length;
			this.identifier = identifier;
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
	    public long getMessageType() {
	        return type;
	    }
	   
	    /**
	     * @return the length
	     */
	    public long getLength() {
	        return length;
	    }

		/**
		 * @return the identifier
		 */
	    @Override
		public long getIdentifier() {
			return this.identifier;
		}
	
	    
}
