package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
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
		this.length = PaAttributeTlvFixedLengthEnum.MESSAGE.length();
	}


	@Override
	public PaAttributeHeaderBuilder setFlags(final byte flags){
		if ((byte)(flags & 0x80) == PaAttributeFlagsEnum.NOSKIP.bit()) {
			this.flags = new PaAttributeFlagsEnum[]{PaAttributeFlagsEnum.NOSKIP};
		}
		
		return this;
	}

	@Override
	public PaAttributeHeaderBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}

	@Override
	public PaAttributeHeaderBuilder setType(final long type) throws RuleException{
		
		TypeReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}

	@Override
	public PaAttributeHeaderBuilder setLength(final long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		this.length = length;
		
		return this;
	}
	
	@Override
	public PaAttributeHeader toAttributeHeader(){
		
		PaAttributeHeader mHead = new PaAttributeHeader(this.flags, this.vendorId, this.type, this.length);
		
		return mHead;
	}

	@Override
	public PaAttributeHeaderBuilder clear() {

		return new PaAttributeHeaderBuilderIetf();
	}
}
