package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.message.PaMessageHeaderBuilderIetf;

public class PaAttributeValueErrorInformationInvalidParamBuilderIetf implements
		PaAttributeValueErrorInformationInvalidParamBuilder {
	
	
	private long length;
	private PaMessageHeader messageHeader;
	private long offset;
	
	public PaAttributeValueErrorInformationInvalidParamBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.ERR_INF.length() + PaAttributeTlvFixedLength.MESSAGE.length() + 4; // 4 = offset
		this.messageHeader = new PaMessageHeaderBuilderIetf().toMessageHeader();
		this.offset = 0;
	}

	@Override
	public void setMessageHeader(PaMessageHeader messageHeader) {
		if(messageHeader != null){
			this.messageHeader = messageHeader;
		}
	}

	@Override
	public void setOffset(long offset) {
		this.offset = offset;
	}

	@Override
	public PaAttributeValueErrorInformationInvalidParam toValue(){
		
		return new PaAttributeValueErrorInformationInvalidParam(length, messageHeader, offset);
	}

	@Override
	public PaAttributeValueErrorInformationInvalidParamBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationInvalidParamBuilderIetf();
	}

}
