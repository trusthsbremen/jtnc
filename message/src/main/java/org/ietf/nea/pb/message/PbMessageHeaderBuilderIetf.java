package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.CommonLengthLimits;
import org.ietf.nea.pb.validate.rules.TypeReservedAndLimits;
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
		this.length = PbMessageTlvFixedLengthEnum.MESSAGE.length();
	}

	@Override
	public PbMessageHeaderBuilder setFlags(final byte flags){
		if ((byte)(flags & 0x80) == PbMessageFlagsEnum.NOSKIP.bit()) {
			this.flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		}
		
		return this;
	}

	@Override
	public PbMessageHeaderBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}

	@Override
	public PbMessageHeaderBuilder setType(final long type) throws RuleException{
		
		TypeReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}

	@Override
	public PbMessageHeaderBuilder setLength(final long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		this.length = length;
		
		return this;
	}

	@Override
	public PbMessageHeader toMessageHeader(){
		
		PbMessageHeader mHead = new PbMessageHeader(this.flags, this.vendorId, this.type, this.length);
		
		return mHead;
	}

	@Override
	public PbMessageHeaderBuilder clear() {
		return new PbMessageHeaderBuilderIetf();
	}
}
