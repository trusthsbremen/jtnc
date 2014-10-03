package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.CommonLengthLimits;
import org.ietf.nea.pb.validate.rules.MessageReservedAndLimits;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageHeaderBuilderIetf implements PbMessageHeaderBuilder{

	private PbMessageFlagsEnum[] flags;
	private long vendorId;
	private long type;
	private long length;

	public PbMessageHeaderBuilderIetf(){
		this.flags = new PbMessageFlagsEnum[0];
		this.vendorId = IETFConstants.IETF_PEN_VENDORID;
		this.type = PbMessageTypeEnum.IETF_PB_PA.messageType();
		this.length = PbMessageTlvFixedLength.MESSAGE.length();
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setFlags(byte)
	 */
	@Override
	public PbMessageHeaderBuilder setFlags(final byte flags){
		if ((byte)(flags & 0x80) == PbMessageFlagsEnum.NOSKIP.bit()) {
			this.flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		}
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setVendorId(long)
	 */
	@Override
	public PbMessageHeaderBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setType(long)
	 */
	@Override
	public PbMessageHeaderBuilder setType(final long type) throws RuleException{
		
		MessageReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setLength(long)
	 */
	@Override
	public PbMessageHeaderBuilder setLength(final long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		this.length = length;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#toMessage()
	 */
	@Override
	public PbMessageHeader toMessageHeader(){
		
		PbMessageHeader mHead = new PbMessageHeader(this.flags, this.vendorId, this.type, this.length);
		
		return mHead;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#clear()
	 */
	@Override
	public PbMessageHeaderBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageHeaderBuilderIetf();
	}
}
