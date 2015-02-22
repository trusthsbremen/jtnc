package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.validate.rules.ErrorCodeLimits;
import org.ietf.nea.pb.validate.rules.ErrorVendorIdLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueErrorBuilderIetf implements PbMessageValueErrorBuilder{
	
	private PbMessageErrorFlagsEnum[] errorFlags;  //  8 bit(s) 
    private long errorVendorId;                    // 24 bit(s)
    private int errorCode;                         // 16 bit(s)
    private long length;
    private AbstractPbMessageValueErrorParameter errorParameter; //32 bit(s)
    
    public PbMessageValueErrorBuilderIetf(){
    	this.errorFlags = new PbMessageErrorFlagsEnum[0];
    	this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.errorCode = PbMessageErrorCodeEnum.IETF_LOCAL.code();
    	this.length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length();
    	this.errorParameter = null;
    }

	@Override
	public PbMessageValueErrorBuilder setErrorFlags(byte errorFlags) {
		
		if ((byte)(errorFlags & 0x80)  == PbMessageErrorFlagsEnum.FATAL.bit()) {
			this.errorFlags = new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL};
		}
		
		return this;
	}

	@Override
	public PbMessageValueErrorBuilder setErrorVendorId(long errorVendorId) throws RuleException {
		
		ErrorVendorIdLimits.check(errorVendorId);
		this.errorVendorId = errorVendorId;
		
		return this;
	}

	@Override
	public PbMessageValueErrorBuilder setErrorCode(int errorCode) throws RuleException {
		
		ErrorCodeLimits.check(errorCode);
		this.errorCode = errorCode;
		
		return this;
	}

	@Override
	public PbMessageValueErrorBuilder setErrorParameter(AbstractPbMessageValueErrorParameter errorParameter) {
		
		if( errorParameter != null){
			this.errorParameter = errorParameter;
			this.length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length() + errorParameter.getLength();
		}
		
		return this;
	}

	@Override
	public PbMessageValueError toObject(){
//	    Error parameter can be null
//		if(this.errorParameter == null){
//			throw new IllegalStateException("A error value has to be set.");
//		}
		
		return new PbMessageValueError(this.errorFlags, this.errorVendorId, this.errorCode, this.length, this.errorParameter);
	}

	@Override
	public PbMessageValueErrorBuilder newInstance() {

		return new PbMessageValueErrorBuilderIetf();
	}

}
