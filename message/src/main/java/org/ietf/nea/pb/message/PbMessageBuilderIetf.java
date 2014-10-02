package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.EnumSet;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.PbMessageNoSkip;
import org.ietf.nea.pb.validate.rules.PaMessageUnknownButNoSkip;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbMessageBuilderIetf implements PbMessageBuilder {

	private PbMessageFlagsEnum[] flags;
	private long vendorId;
	private long type;
	private long length;
	private AbstractPbMessageValue value;

	public PbMessageBuilderIetf(){
		this.flags = new PbMessageFlagsEnum[0];
		this.vendorId = IETFConstants.IETF_PEN_VENDORID;
		this.type = PbMessageTypeEnum.IETF_PB_PA.messageType();
		this.length = PbMessageTlvFixedLength.MESSAGE.length();
		this.value = null;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setFlags(byte)
	 */
	@Override
	public PbMessageBuilder setFlags(final byte flags){
		if ((byte)(flags & 0x80) == PbMessageFlagsEnum.NOSKIP.bit()) {
			this.flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		}
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setVendorId(long)
	 */
	@Override
	public PbMessageBuilder setVendorId(final long vendorId) throws RuleException{
		
		VendorIdReservedAndLimits.check(vendorId);
		this.vendorId = vendorId;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setType(long)
	 */
	@Override
	public PbMessageBuilder setType(final long type) throws RuleException{
		
		VendorIdReservedAndLimits.check(type);
		this.type = type;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setValue(org.ietf.nea.pb.message.AbstractPbMessageValue)
	 */
	@Override
	public PbMessageBuilder setValue(final AbstractPbMessageValue value){
		
		if(value != null){
			this.addValueAndCheckLength(value);
		}
		
		return this;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.message.TnccMessageBuilder#toMessage()
	 */
	@Override
	public PbMessage toMessage() throws RuleException{
		
		if(value == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		EnumSet<PbMessageFlagsEnum> tempFlags;
		if(flags.length > 0){
			tempFlags = EnumSet.copyOf(Arrays.asList(flags));
		}else{
			tempFlags = EnumSet.noneOf(PbMessageFlagsEnum.class);
		}
		
		PbMessageNoSkip.check(this.value,tempFlags);
		PaMessageUnknownButNoSkip.check(this.value, tempFlags);
		// TODO if necessary make a message length check here, first finding the correct message type the the length parameter
		// it seems not necessary, because the length is set by the content.
		
		PbMessage message = new PbMessage(this.flags, this.vendorId, this.type, this.length, this.value);
		
		return message;
	}
	
	private void addValueAndCheckLength(AbstractPbMessageValue value){
		long valueLength = value.getLength();
		if(valueLength > 0 && (IETFConstants.IETF_MAX_LENGTH - valueLength) < this.length){
			throw new ArithmeticException("Message size is to large.");
		}
		this.length = PbMessageTlvFixedLength.MESSAGE.length() + valueLength;
		
		this.value = value;
}

	@Override
	public PbMessageBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageBuilderIetf();
	}
}
