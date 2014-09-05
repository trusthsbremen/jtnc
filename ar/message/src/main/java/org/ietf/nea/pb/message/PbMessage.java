package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;



public class PbMessage implements TnccsMessage {
    public static final byte FIXED_LENGTH = 12;
    
    private final EnumSet<PbMessageFlagsEnum> flags;                               //  8 bit(s)
    private final long vendorId;                                // 24 bit(s)
    private final long type;                                    // 32 bit(s)
    private final long length;// 32 bit(s) min value is 12 for the 12 bytes in this header 
    private final AbstractPbMessageValue value;

    PbMessage(PbMessageFlagsEnum[] flags, long vendorId, long type, AbstractPbMessageValue value) {
    	if(flags.length > 0){
         	this.flags = EnumSet.copyOf(Arrays.asList(flags));
        }else {
        	this.flags = EnumSet.noneOf(PbMessageFlagsEnum.class);
        }
    	this.vendorId = vendorId;
		this.type = type;
		this.value = value;
		this.length = value.getLength() + FIXED_LENGTH;
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
     * @return the length
     */
    public long getLength() {
        return length;
    }
    
	/**
	 * @return the value
	 */
	public AbstractPbMessageValue getValue() {
		return this.value;
	}
	

    
    
}
