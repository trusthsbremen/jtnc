package org.ietf.nea.pb.message;

import java.util.Arrays;
import java.util.EnumSet;

import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.PaMessageNoSkip;
import org.ietf.nea.pb.validate.rules.VendorIdReservedAndLimits;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

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
		this.length = PbMessage.FIXED_LENGTH;
		this.value = null;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setFlags(byte)
	 */
	@Override
	public void setFlags(final byte flags){
		
		if ((flags & 0x80) == PbMessageFlagsEnum.NOSKIP.bit()) {
			this.flags = new PbMessageFlagsEnum[]{PbMessageFlagsEnum.NOSKIP};
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setVendorId(long)
	 */
	@Override
	public void setVendorId(final long vendorId) throws ValidationException{
		VendorIdReservedAndLimits.check(vendorId);
		
		this.vendorId = vendorId;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setType(long)
	 */
	@Override
	public void setType(final long type) throws ValidationException{
		VendorIdReservedAndLimits.check(type);
		
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#setValue(org.ietf.nea.pb.message.AbstractPbMessageValue)
	 */
	@Override
	public void setValue(final AbstractPbMessageValue value){
		if(value != null){
			this.addValueAndCheckLength(value);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.message.TnccMessageBuilder#toMessage()
	 */
	@Override
	public PbMessage toMessage() throws ValidationException{
		if(value == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		PaMessageNoSkip.check(this.value, EnumSet.copyOf(Arrays.asList(flags)));
		
		// TODO if necessary make a message length check here, first finding the correct message type the the length parameter
		// it seems not necessary, because the length is set by the content.
		
		PbMessage message = new PbMessage(flags, vendorId, type, value);
		
		return message;
	}
	
	private void addValueAndCheckLength(AbstractPbMessageValue value){
		long valueLength = value.getLength();
		if(valueLength > 0 && (IETFConstants.IETF_MAX_LENGTH - valueLength) < this.length){
			throw new ArithmeticException("Message size is to large.");
		}
		this.length += valueLength;
		this.value = value;
}

	@Override
	public PbMessageBuilder clear() {
		// TODO Auto-generated method stub
		return new PbMessageBuilderIetf();
	}
}
