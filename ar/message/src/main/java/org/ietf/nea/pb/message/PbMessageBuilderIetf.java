package org.ietf.nea.pb.message;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

public class PbMessageBuilderIetf {

	PbMessageFlagsEnum[] flags = new PbMessageFlagsEnum[0];
	long vendorId = IETFConstants.IETF_PEN_VENDORID;
	long type = PbMessageTypeEnum.IETF_PB_PA.messageType();
	long length = PbMessage.FIXED_LENGTH;
	AbstractPbMessageValue value = null;

	//TODO make more error checking e.g. check if the size of the long values correspondes to the
	// sizes defined in the RFCs.

	public void setFlags(PbMessageFlagsEnum[] flags){
		this.flags = flags;
	}
	
	public void setVendorId(long vendorId){
		this.vendorId = vendorId;
	}
	
	public void setType(long type){
		this.type = type;
	}
	
	public void setValue(AbstractPbMessageValue value){
		this.value = value;
	}
	
	public PbMessage toMessage(){
		if(value == null){
			throw new IllegalStateException("A message value has to be set.");
		}
		
		PbMessage message = new PbMessage(flags, vendorId, type, value);
		return message;
	}
	
}
