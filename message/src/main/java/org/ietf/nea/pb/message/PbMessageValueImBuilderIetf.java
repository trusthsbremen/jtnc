package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.ImIdLimits;
import org.ietf.nea.pb.validate.rules.ImMessageTypeReservedAndLimits;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueImBuilderIetf implements PbMessageValueImBuilder{

	private PbMessageImFlagsEnum[] imFlags; //  8 bit(s)
	   
    private long subVendorId;                                           // 24 bit(s)
    private long subType;                                               // 32 bit(s)
    private long collectorId;                                            // 16 bit(s)
    private long validatorId;                                            // 16 bit(s)
    private long length;
    
    private byte[] message; //ImMessage as byte[]

    public PbMessageValueImBuilderIetf(){
    	this.imFlags = new PbMessageImFlagsEnum[0];
    	this.subVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.subType = 0;
    	this.collectorId = TNCConstants.TNC_IMCID_ANY;
    	this.validatorId = TNCConstants.TNC_IMVID_ANY;
    	this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length();
    	this.message = new byte[0];
    }
    
	@Override
	public PbMessageValueImBuilder setImFlags(byte imFlags) {
		
		if ((byte)(imFlags & 0x80)  == PbMessageImFlagsEnum.EXCL.bit()) {
			this.imFlags = new PbMessageImFlagsEnum[]{PbMessageImFlagsEnum.EXCL};
		}
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setSubVendorId(long subVendorId) throws RuleException {
		
		ImMessageTypeReservedAndLimits.check(subVendorId);
		this.subVendorId = subVendorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setSubType(long subType) throws RuleException {
		
		ImMessageTypeReservedAndLimits.check(subType);
		this.subType = subType;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setCollectorId(long collectorId) throws RuleException {
		
		ImIdLimits.check(collectorId);
		this.collectorId = collectorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setValidatorId(long validatorId) throws RuleException {
		
		ImIdLimits.check(validatorId);
		this.validatorId = validatorId;
		
		return this;
	}

	@Override
	public PbMessageValueImBuilder setMessage(byte[] message) {
		
		if(message != null){
			this.message = message;
			this.length = PbMessageTlvFixedLengthEnum.IM_VALUE.length() + message.length;
		}
		
		return this;
	}

	@Override
	public PbMessageValueIm toValue(){

		return new PbMessageValueIm(this.imFlags, this.subVendorId, this.subType, this.collectorId, this.validatorId, this.length, this.message);
	}

	@Override
	public PbMessageValueImBuilder clear() {

		return new PbMessageValueImBuilderIetf();
	}

}
