package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;

/**
 * Reference IETF RFC 5793 section 4.5:
 * ------------------------------------
 * The PB-TNC message type named PB-PA (value 1) contains one PA
 * message.  Many batches will contain several PB-PA messages, but some
 * batches may not contain any messages of this type.
 * 
 * All Posture Broker Client and Posture Broker Server implementations
 * MUST implement support for this PB-TNC message type.  Generally, this
 * support will consist of forwarding the enclosed PA message to the
 * appropriate Posture Collectors and Posture Validators.
 * 
 * The type of the PA message contained in a PB-PA message is indicated
 * by the PA Message Vendor ID and PA Subtype fields.
 * 
 * The NOSKIP flag in the PB-TNC Message Header MUST be set for this
 * message type.  Any Posture Broker Client or Posture Broker Server
 * that receives a PB-PA message with the NOSKIP flag not set MUST
 * ignore the message and MUST respond with a fatal Invalid Parameter
 * error code in a CLOSE batch.
 * 
 * The PB-TNC Message Length field MUST contain the length of the entire
 * PB-TNC message, including the fixed-length fields at the start of the
 * PB-TNC message (the fields Flags, PB-TNC Vendor ID, PB-TNC Message
 * Type, and PB-TNC Message Length), the fixed-length fields listed
 * below (Flags, PA Message Vendor ID, PA Subtype, Posture Collector
 * Identifier, and Posture Validator Identifier), and the PA Message
 * Body.  Since the PA Message Body is variable length, the value in the
 * PB-TNC Message Length field will vary also.  However, it MUST always
 * be at least 24 to cover the fixed-length fields listed in the
 * preceding sentences.
 * 
 */

public class PbMessageValueIm extends AbstractPbMessageValue{

    public static final byte FIXED_LENGTH = 12;
    
    protected final EnumSet<PbMessageImFlagsEnum> imFlags; //  8 bit(s)
   
    private final long subVendorId;                                           // 24 bit(s)
    private final long subType;                                               // 32 bit(s)
    private final long collectorId;                                            // 16 bit(s)
    private final long validatorId;                                            // 16 bit(s)
    
    private final byte[] message; //ImMessage as byte[]

   
    
    
    public PbMessageValueIm(PbMessageImFlagsEnum[] flags, long subVendorId, long subType,
			long collectorId, long validatorId, byte[] message) {
		super(FIXED_LENGTH + message.length);
		
		if(flags != null && flags.length > 0){
	        	this.imFlags = EnumSet.copyOf(Arrays.asList(flags));
	    }else{
	    	this.imFlags = EnumSet.noneOf(PbMessageImFlagsEnum.class);
	    }
		this.subVendorId = subVendorId;
		this.subType = subType;
		this.collectorId = collectorId;
		this.validatorId = validatorId;
		this.message = message;
	}
	/**
     * @return the flags
     */
    public Set<PbMessageImFlagsEnum> getImFlags() {
        return Collections.unmodifiableSet(imFlags);
    }



    /**
     * @return the subVendorId
     */
    public long getSubVendorId() {
        return subVendorId;
    }


 

    /**
     * @return the subType
     */
    public long getSubType() {
        return subType;
    }


    /**
     * @return the collectorId
     */
    public long getCollectorId() {
        return collectorId;
    }


    /**
     * @return the validatorId
     */
    public long getValidatorId() {
        return validatorId;
    }


    /**
     * @return the message
     */
    public byte[] getMessage() {
        return Arrays.copyOf(this.message, this.message.length);
    }

}
