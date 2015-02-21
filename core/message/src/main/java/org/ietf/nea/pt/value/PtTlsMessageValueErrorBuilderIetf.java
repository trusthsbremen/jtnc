package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pt.validate.rules.VendorIdReservedAndLimits;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class PtTlsMessageValueErrorBuilderIetf implements PtTlsMessageValueErrorBuilder{
	
    private long errorVendorId;                    // 24 bit(s)
    private long errorCode;                        // 32 bit(s)
    private long length;
    private byte[] message;
    
    public PtTlsMessageValueErrorBuilderIetf(){
    	this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.errorCode = PtTlsMessageErrorCodeEnum.IETF_RESERVED.code();
    	this.length = PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length();
    	this.message = null;
    }

	@Override
	public PtTlsMessageValueErrorBuilder setErrorVendorId(long errorVendorId) throws RuleException {
		
		VendorIdReservedAndLimits.check(errorVendorId);
		this.errorVendorId = errorVendorId;
		
		return this;
	}

	@Override
	public PtTlsMessageValueErrorBuilder setErrorCode(long errorCode) throws RuleException {
		
		TypeReservedAndLimits.check(errorCode);
		this.errorCode = errorCode;
		
		return this;
	}

	@Override
	public PtTlsMessageValueErrorBuilder setPartialMessage(byte[] message) {
		
		if( message != null){
			this.message = message;
			this.length = PtTlsMessageTlvFixedLengthEnum.ERR_VALUE.length() + message.length;
		}
		
		return this;
	}

	@Override
	public PtTlsMessageValueError toObject(){
		if(this.message == null){
			throw new IllegalStateException("The faulty message has to be copied and set.");
		}
		
		return new PtTlsMessageValueError(this.errorVendorId, this.errorCode, this.length, this.message);
	}

	@Override
	public PtTlsMessageValueErrorBuilder newInstance() {

		return new PtTlsMessageValueErrorBuilderIetf();
	}

}
