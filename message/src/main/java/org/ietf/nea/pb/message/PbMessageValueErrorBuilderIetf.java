package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.validate.rules.ErrorCodeLimits;
import org.ietf.nea.pb.validate.rules.ErrorVendorIdLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageValueErrorBuilderIetf implements PbMessageValueErrorBuilder{
	
	private PbMessageErrorFlagsEnum[] errorFlags; //  8 bit(s) 
    private long errorVendorId;                                           // 24 bit(s)
    private short errorCode;                                                // 16 bit(s)
    private long length;
    private AbstractPbMessageValueErrorParameter errorParameter; //32 bit(s) , may be (1) (one field full 32 bit length) if offset or (4) (4 fields every field has 8 bit length) if version information is needed.
    
    public PbMessageValueErrorBuilderIetf(){
    	this.errorFlags = new PbMessageErrorFlagsEnum[0];
    	this.errorVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.errorCode = PbMessageErrorCodeEnum.IETF_LOCAL.code();
    	this.length = PbMessageTlvFixedLength.ERR_VALUE.length();
    	this.errorParameter = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueErrorBuilder#setErrorFlags(byte)
	 */
	@Override
	public PbMessageValueErrorBuilder setErrorFlags(byte errorFlags) {
		
		if ((byte)(errorFlags & 0x80)  == PbMessageErrorFlagsEnum.FATAL.bit()) {
			this.errorFlags = new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL};
		}
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueErrorBuilder#setErrorVendorId(long)
	 */
	@Override
	public PbMessageValueErrorBuilder setErrorVendorId(long errorVendorId) throws RuleException {
		
		ErrorVendorIdLimits.check(errorVendorId);
		this.errorVendorId = errorVendorId;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueErrorBuilder#setErrorCode(short)
	 */
	@Override
	public PbMessageValueErrorBuilder setErrorCode(short errorCode) throws RuleException {
		
		ErrorCodeLimits.check(errorCode);
		this.errorCode = errorCode;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueErrorBuilder#setErrorParameter(byte[])
	 */
	@Override
	public PbMessageValueErrorBuilder setErrorParameter(AbstractPbMessageValueErrorParameter errorParameter) {
		
		if( errorParameter != null){
			this.errorParameter = errorParameter;
			this.length = PbMessageTlvFixedLength.ERR_VALUE.length() + errorParameter.getLength();
		}
		
		return this;
	}

	@Override
	public PbMessageValueError toValue(){
		if(this.errorParameter == null){
			throw new IllegalStateException("A error value has to be set.");
		}
		
		return new PbMessageValueError(this.errorFlags, this.errorVendorId, this.errorCode, this.length, this.errorParameter);
	}

	@Override
	public PbMessageValueErrorBuilder clear() {

		return new PbMessageValueErrorBuilderIetf();
	}

}
