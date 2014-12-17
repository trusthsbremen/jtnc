package org.ietf.nea.pt.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.validate.rules.CommonLengthLimits;
import org.ietf.nea.pt.validate.rules.IdentifierLimits;
import org.ietf.nea.pt.validate.rules.TypeReservedAndLimits;
import org.ietf.nea.pt.validate.rules.VendorIdReservedAndLimits;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class PtTlsMessageHeaderBuilderIetf implements PtTlsMessageHeaderBuilder{

	private long vendorId;
	private long type;
	private long length;
	private long identifier;

	public PtTlsMessageHeaderBuilderIetf(){
		this.vendorId = IETFConstants.IETF_PEN_VENDORID;
		this.type = PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.messageType();
		this.length = PtTlsMessageTlvFixedLengthEnum.MESSAGE.length();
	}

	@Override
	public PtTlsMessageHeaderBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}

	@Override
	public PtTlsMessageHeaderBuilder setType(final long type) throws RuleException{
		
		TypeReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}

	@Override
	public PtTlsMessageHeaderBuilder setLength(final long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		this.length = length;
		
		return this;
	}

	@Override
	public PtTlsMessageHeaderBuilder setIdentifier(final long identifier) throws RuleException{
		
		IdentifierLimits.check(identifier);
		this.identifier = identifier;
		
		return this;
	}
	
	@Override
	public PtTlsMessageHeader toObject(){
		
		PtTlsMessageHeader mHead = new PtTlsMessageHeader(this.vendorId, this.type, this.length, this.identifier);
		
		return mHead;
	}

	@Override
	public PtTlsMessageHeaderBuilder newInstance() {
		return new PtTlsMessageHeaderBuilderIetf();
	}

}
