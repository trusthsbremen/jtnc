package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageHeader;



public class PbMessageHeader implements TnccsMessageHeader {
    
    private final EnumSet<PbMessageFlagsEnum> flags;            //  8 bit(s)
    private final long vendorId;                                // 24 bit(s)
    private final long type;                                    // 32 bit(s)
    private final long length;									// 32 bit(s) min value is 12 for the 12 bytes in this header 

    PbMessageHeader(final PbMessageFlagsEnum[] flags, final long vendorId, final long type, final long length) {
    	if(flags.length > 0){
         	this.flags = EnumSet.copyOf(Arrays.asList(flags));
        }else {
        	this.flags = EnumSet.noneOf(PbMessageFlagsEnum.class);
        }
    	this.vendorId = vendorId;
		this.type = type;
		this.length = length;
	}
    
	/**
     * @return the flags
     */
    public Set<PbMessageFlagsEnum> getFlags() {
        return Collections.unmodifiableSet(flags);
    }
   
    /**
     * @return the vendorId
     */
    @Override
    public long getVendorId() {
        return vendorId;
    }
   
    /**
     * @return the type
     */
    @Override
    public long getMessageType() {
        return type;
    }
   
    /**
     * @return the length
     */
    public long getLength() {
        return length;
    }

}
