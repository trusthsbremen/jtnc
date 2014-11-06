package de.hsbremen.tc.tnc.im.module;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.IETFConstants;

public class SupportedMessageTypeFactory {

	
	
	public static SupportedMessageType createSupportedMessageType(long vendorId, long type){
		
		if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
	      	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
	    }
	    if(type > IETFConstants.IETF_MAX_TYPE){
	    	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
	    }
	    
	    return new SupportedMessageType(vendorId, type);
	}
	
	public static List<SupportedMessageType> createSupportedMessageTypes(long[] vendorIds, long[] messageTypes){
		List<SupportedMessageType> sTypes = new LinkedList<>();
		
		if(vendorIds != null && messageTypes != null){
			
			if(vendorIds.length != messageTypes.length){
				throw new IllegalArgumentException("The supplied arrays have a different length (" + vendorIds.length + ":" + messageTypes + ").");
			}
			
			for (int i = 0; i < vendorIds.length; i++) {
				SupportedMessageType mType = createSupportedMessageType(vendorIds[i], messageTypes[i]);
				sTypes.add(mType);
			}
		}
		
		return sTypes;
	}
	
	public static SupportedMessageType createSupportedMessageTypeLegacy(long messageType){
		long vendorId = messageType >>> 8;
		long type = messageType & 0xFF;
		
		return createSupportedMessageType(vendorId, type);
	}
	
	public static List<SupportedMessageType> createSupportedMessageTypesLegacy(long[] messageTypes){

		List<SupportedMessageType> types = new LinkedList<>();
		if(messageTypes != null){
			for (long l : messageTypes) {
				types.add(createSupportedMessageTypeLegacy(l));
			}
		}

		return types;
	}
	
	
	public static long[] createSupportedMessageTypeArrayLegacy(Set<SupportedMessageType> supportedMessageTypes){
		
		if(supportedMessageTypes == null || supportedMessageTypes.size() <= 0){
			return new long[0];
		}
		
		long[] types = new long[supportedMessageTypes.size()];
		int i = 0;
		for (SupportedMessageType supportedMessageType : supportedMessageTypes) {
			long messageType = (supportedMessageType.getVendorId() << 8) | (supportedMessageType.getType() & 0xFF);
			types[i] = messageType;
		}
		
		return types;
	}
	
	public static long[][] createSupportedMessageTypeArray(Set<SupportedMessageType> supportedMessageTypes){
		
		
		if(supportedMessageTypes == null || supportedMessageTypes.size() <= 0){
			return new long[2][0];
		}
		
		long[][] types = new long[2][supportedMessageTypes.size()];
		int i = 0;
		for (SupportedMessageType supportedMessageType : supportedMessageTypes) {
			types[0][i] = supportedMessageType.getVendorId();
			types[1][i] = supportedMessageType.getType();
		}
		
		return types;
	}
	
	
}
