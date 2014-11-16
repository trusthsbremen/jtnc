package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;

public class PaAttributeValueErrorInformationInvalidParamBuilderIetf implements
		PaAttributeValueErrorInformationInvalidParamBuilder {
	
	
	private long length;
	private RawMessageHeader messageHeader;
	private long offset;
	
	public PaAttributeValueErrorInformationInvalidParamBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.ERR_INF.length() + PaAttributeTlvFixedLength.MESSAGE.length() + 4; // 4 = offset
		this.messageHeader = new RawMessageHeader((short)0, new byte[0], 0L);
		this.offset = 0;
	}

	@Override
	public void setMessageHeader(RawMessageHeader messageHeader) {
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
