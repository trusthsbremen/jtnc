package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.validate.rules.ImIdLimits;
import org.ietf.nea.pb.validate.rules.ImMessageTypeReservedAndLimits;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageValueImBuilderIetf implements PbMessageValueImBuilder{

	private PbMessageImFlagsEnum[] imFlags; //  8 bit(s)
	   
    private long subVendorId;                                           // 24 bit(s)
    private long subType;                                               // 32 bit(s)
    private long collectorId;                                            // 16 bit(s)
    private long validatorId;                                            // 16 bit(s)
    
    private byte[] message; //ImMessage as byte[]

    public PbMessageValueImBuilderIetf(){
    	this.imFlags = new PbMessageImFlagsEnum[0];
    	this.subVendorId = IETFConstants.IETF_PEN_VENDORID;
    	this.subType = 0;
    	this.collectorId = TNCConstants.TNC_IMCID_ANY;
    	this.validatorId = TNCConstants.TNC_IMVID_ANY;
    	this.message = new byte[0];
    }
    
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setImFlags(byte)
	 */
	@Override
	public void setImFlags(byte imFlags) {
		if ((imFlags & 0x80)  == PbMessageImFlagsEnum.EXCL.bit()) {
			this.imFlags = new PbMessageImFlagsEnum[]{PbMessageImFlagsEnum.EXCL};
		}
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setSubVendorId(long)
	 */
	@Override
	public void setSubVendorId(long subVendorId) throws ValidationException {
		
		ImMessageTypeReservedAndLimits.check(subVendorId);
		this.subVendorId = subVendorId;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setSubType(long)
	 */
	@Override
	public void setSubType(long subType) throws ValidationException {
		
		ImMessageTypeReservedAndLimits.check(subType);
		this.subType = subType;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setCollectorId(long)
	 */
	@Override
	public void setCollectorId(long collectorId) throws ValidationException {
		
		ImIdLimits.check(collectorId);
		this.collectorId = collectorId;
	
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setValidatorId(long)
	 */
	@Override
	public void setValidatorId(long validatorId) throws ValidationException {
		
		ImIdLimits.check(validatorId);
		this.validatorId = validatorId;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#setMessage(byte[])
	 */
	@Override
	public void setMessage(byte[] message) {
		
		if(message != null){
			this.message = message;
		}
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#toValue()
	 */
	@Override
	public PbMessageValueIm toValue() throws ValidationException {

		return new PbMessageValueIm(imFlags, subVendorId, subType, collectorId, validatorId, message);
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueImBuilder#clear()
	 */
	@Override
	public PbMessageValueImBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageValueImBuilderIetf();
	}

}
