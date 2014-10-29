package org.ietf.nea.pa.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;

import de.hsbremen.tc.tnc.m.attribute.ImAttributeHeader;

public class PaAttributeHeader implements ImAttributeHeader {

	private final EnumSet<PaAttributeFlagsEnum> flags;           //  8 bit(s)
	private final long vendorId;                                 // 24 bit(s)
	private final long type;                                     // 32 bit(s)
	private final long length;									 // 32 bit(s) min value is 12 for the 12 bytes in this header 

	PaAttributeHeader(PaAttributeFlagsEnum[] flags, long vendorId,
			long type, long length) {
		if(flags.length > 0){
         	this.flags = EnumSet.copyOf(Arrays.asList(flags));
        }else {
        	this.flags = EnumSet.noneOf(PaAttributeFlagsEnum.class);
        }
		this.vendorId = vendorId;
		this.type = type;
		this.length = length;
	}

	/**
     * @return the flags
     */
    public Set<PaAttributeFlagsEnum> getFlags() {
        return Collections.unmodifiableSet(flags);
    }
	
	@Override
	public long getVendorId() {
		return this.vendorId;
	}

	@Override
	public long getAttributeType() {
		return this.type;
	}
   
    /**
     * @return the length
     */
    public long getLength() {
        return length;
    }
	
}
