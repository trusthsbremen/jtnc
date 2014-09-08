package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;

/**
 * Reference IETF RFC 5793 section 4.9:
 * ------------------------------------
 * The PB-TNC message type named PB-Error (value 5) is used by the
   Posture Broker Client or Posture Broker Server to indicate that an
   error has occurred.  The Posture Broker Client or Posture Broker
   Server MAY include one or more messages of this type in any batch of
   any type.  Other messages may also be included in the same batch.
   The party that receives a PB-Error message MAY log it or take other
   action as deemed appropriate.  If the FATAL flag is set (value 1),
   the recipient MUST terminate the PB-TNC session after processing the
   batch without sending any messages in response.  Every Posture Broker
   Client and Posture Broker Server MUST implement this message type.
   
   The NOSKIP flag in the PB-TNC Message Header MUST be set for this
   message type.
   
   Since the Error Parameters field is variable length, the value in 
   the PB-TNC Message Length field will vary also. However, it MUST 
   always be at least 20 to cover the fixed-length fields.
 */
public class PbMessageValueError extends AbstractPbMessageValue {

    public static final byte FIXED_LENGTH = 8;
    
    private final EnumSet<PbMessageErrorFlagsEnum> errorFlags; //  8 bit(s) 
    private final long errorVendorId;                                           // 24 bit(s)
    private final short errorCode;                                                // 16 bit(s)
    private final short reserved;                                         // 16 bit(s) should be 0
    private byte[] errorParameter; //32 bit(s) , may be (1) (one field full 32 bit length) if offset or (4) (4 fields every field has 8 bit length) if version information is needed.
    
    

	PbMessageValueError(PbMessageErrorFlagsEnum[] flags, long errorVendorId,
			short errorCode, short reserved,
			byte[] errorParameter) {
		super(FIXED_LENGTH + errorParameter.length);
		
		if(flags != null && flags.length > 0){
	       	this.errorFlags = EnumSet.copyOf(Arrays.asList(flags));
	    }else{
	     	this.errorFlags = EnumSet.noneOf(PbMessageErrorFlagsEnum.class);
	    }

		this.errorVendorId = errorVendorId;
		this.errorCode = errorCode;
		this.reserved = reserved;
		this.errorParameter = errorParameter;
	}

	/**
	 * @return the errorFlags
	 */
	public Set<PbMessageErrorFlagsEnum> getErrorFlags() {
		return Collections.unmodifiableSet(this.errorFlags);
	}

	/**
	 * @return the errVendorId
	 */
	public long getErrorVendorId() {
		return this.errorVendorId;
	}


	/**
	 * @return the errCode
	 */
	public short getErrorCode() {
		return this.errorCode;
	}


	/**
	 * @return the content
	 */
	public byte[] getErrorParameter() {
		return Arrays.copyOf(this.errorParameter, this.errorParameter.length);
	}

	/**
	 * @return the reserved
	 */
	public short getReserved() {
		return this.reserved;
	}
    
    
}
