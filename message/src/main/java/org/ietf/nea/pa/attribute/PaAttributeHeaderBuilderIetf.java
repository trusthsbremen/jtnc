package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.validate.rules.CommonLengthLimits;
import org.ietf.nea.pa.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pa.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PaAttributeHeaderBuilderIetf implements PaAttributeHeaderBuilder{

	private PaAttributeFlagsEnum[] flags;
	private long vendorId;
	private long type;
	private long length;

	public PaAttributeHeaderBuilderIetf(){
		this.flags = new PaAttributeFlagsEnum[0];
		this.vendorId = IETFConstants.IETF_PEN_VENDORID;
		this.type = PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION.attributeType();
		this.length = PaAttributeTlvFixedLength.MESSAGE.length();
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setFlags(byte)
	 */
	@Override
	public PaAttributeHeaderBuilder setFlags(final byte flags){
		if ((byte)(flags & 0x80) == PaAttributeFlagsEnum.NOSKIP.bit()) {
			this.flags = new PaAttributeFlagsEnum[]{PaAttributeFlagsEnum.NOSKIP};
		}
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setVendorId(long)
	 */
	@Override
	public PaAttributeHeaderBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setType(long)
	 */
	@Override
	public PaAttributeHeaderBuilder setType(final long type) throws RuleException{
		
		TypeReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#setLength(long)
	 */
	@Override
	public PaAttributeHeaderBuilder setLength(final long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		this.length = length;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#toMessage()
	 */
	@Override
	public PaAttributeHeader toAttributeHeader(){
		
		PaAttributeHeader mHead = new PaAttributeHeader(this.flags, this.vendorId, this.type, this.length);
		
		return mHead;
	}

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageHeaderBuilder#clear()
	 */
	@Override
	public PaAttributeHeaderBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeHeaderBuilderIetf();
	}
}
