package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class ImComponentFactory {

	public static ImObjectComponent createObjectComponent(final byte imFlags, final long vendorId, final long type, final long collectorId, final long validatorId, final List<? extends ImAttribute> attributes){
		
		if(vendorId >= IETFConstants.IETF_MAX_VENDOR_ID){
	      	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
	    }
	    if(type >= IETFConstants.IETF_MAX_TYPE){
	    	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
	    }
	    
	    ImComponentFlagsEnum[] flags = new ImComponentFlagsEnum[0];
	    if ((byte)(imFlags & 0x80)  == ImComponentFlagsEnum.EXCL.bit()) {
			flags = new ImComponentFlagsEnum[]{ImComponentFlagsEnum.EXCL};
		}
	    
	    return new ImObjectComponent (flags, vendorId, type, collectorId, validatorId, attributes);
	}
	
	public static ImFaultyObjectComponent createFaultyObjectComponent(final byte imFlags, final long vendorId, final long type, final long collectorId, final long validatorId, final List<? extends ImAttribute> attributes, List<ValidationException> excpetions, byte[] messageHeader){
		
		if(vendorId >= IETFConstants.IETF_MAX_VENDOR_ID){
	      	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
	    }
	    if(type >= IETFConstants.IETF_MAX_TYPE){
	    	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
	    }
	    
	    ImComponentFlagsEnum[] flags = new ImComponentFlagsEnum[0];
	    if ((byte)(imFlags & 0x80)  == ImComponentFlagsEnum.EXCL.bit()) {
			flags = new ImComponentFlagsEnum[]{ImComponentFlagsEnum.EXCL};
		}
	    
	    return new ImFaultyObjectComponent(flags, vendorId, type, collectorId, validatorId, attributes, excpetions, messageHeader);
	}
	
	public static ImRawComponent createRawComponent(final byte imFlags, final long vendorId, final long type, final long collectorId, final long validatorId, final byte[] message){
		
		if(vendorId >= IETFConstants.IETF_MAX_VENDOR_ID){
	      	throw new IllegalArgumentException("Vendor ID exceeds its maximum size of " + IETFConstants.IETF_MAX_VENDOR_ID + ".");
	    }
	    if(type >= IETFConstants.IETF_MAX_TYPE){
	    	throw new IllegalArgumentException("Message type exceeds its maximum size of " + IETFConstants.IETF_MAX_TYPE + ".");
	    }

	    return new ImRawComponent(imFlags, vendorId, type, collectorId, validatorId, message);
	}
	
	public static ImRawComponent createLegacyRawComponent(long messageType, byte[] message){
		long vendorId = messageType >>> 8;
		long type = messageType & 0xFF;
		
		return createRawComponent((byte)0, vendorId, type, HSBConstants.HSB_IM_ID_UNKNOWN, HSBConstants.HSB_IM_ID_UNKNOWN, message);
	}
	
	public static ImObjectComponent changeAttributesFromComponent(final ImObjectComponent component, final List<? extends ImAttribute> attributes){
		return new ImObjectComponent (new ArrayList<ImComponentFlagsEnum>(component.getImFlags()).toArray(new ImComponentFlagsEnum[component.getImFlags().size()]), component.getVendorId(), component.getType(), component.getCollectorId(), component.getValidatorId(), attributes);
	}
	
}
