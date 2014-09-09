package org.ietf.nea.pb.message;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.PbMessageValidatorFactroy;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageBuilder;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageBuilderIetf implements TnccsMessageBuilder {

	private final TnccsValidator<PbMessage> validator; 
	
	protected PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	protected long vendorId = IETFConstants.IETF_PEN_VENDORID;
	protected long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
	protected long length = PbMessage.FIXED_LENGTH;
	protected AbstractPbMessageValue value = null;
	
	public PbMessageBuilderIetf(){
		this(PbMessageValidatorFactroy.createDefault());
	}
	
	public PbMessageBuilderIetf(final TnccsValidator<PbMessage> validator){
		this.validator = validator;
	}
	
	public void setFlags(final PbMessageFlagsEnum[] flags){
		if(flags == null){
			throw new NullPointerException("Flags cannot be null.");
		}
		this.flags = flags;
	}
	
	public void setVendorId(final long vendorId){
		if(vendorId > IETFConstants.IETF_MAX_VENDOR_ID){
			throw new IllegalArgumentException("Vendor ID is greater than "+ Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
		}
		
		this.vendorId = vendorId;
	}
	
	public void setType(final long type){
		if(type > IETFConstants.IETF_MAX_TYPE){
			throw new IllegalArgumentException("Type is greater than "+ Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
		}
		
		this.type = type;
	}
	
	public void setValue(final AbstractPbMessageValue value){

		this.value = value;
	}
	
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageBuilder#toMessage()
	 */
	@Override
	public PbMessage toMessage() throws ValidationException{
		if(value == null){
			throw new IllegalStateException("A message value has to be set.");
		}

		PbMessage message = new PbMessage(flags, vendorId, type, value);
	
		validator.validate(message);
		
		return message;
	}
}
