package de.hsbremen.tc.tnc.im.module;

import de.hsbremen.tc.tnc.IETFConstants;

public class SupportedMessageTypeFactory {

	public static SupportedMessageType createSupportedMessageType(long vendorId, long type){
		
		if(vendorId >= IETFConstants.IETF_MAX_VENDOR_ID){
	      	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
	    }
	    if(type >= IETFConstants.IETF_MAX_TYPE){
	    	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
	    }
	    
	    return new SupportedMessageType(vendorId, type);
	}
	
	public static SupportedMessageType createSupportedMessageTypeLegacy(long messageType){
		long vendorId = messageType >>> 8;
		long type = messageType & 0xFF;
		
		return createSupportedMessageType(vendorId, type);
	}
	
	
	
}
