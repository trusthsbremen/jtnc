package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueErrorInformationInvalidParamBuilderIetf implements
		PaAttributeValueErrorInformationInvalidParamBuilder {
	
	
	private long length;
	private RawMessageHeader messageHeader;
	private long offset;
	
	public PaAttributeValueErrorInformationInvalidParamBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length() + PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4; // 4 = offset
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
	public PaAttributeValueErrorInformationInvalidParam toObject(){
		
		return new PaAttributeValueErrorInformationInvalidParam(length, messageHeader, offset);
	}

	@Override
	public PaAttributeValueErrorInformationInvalidParamBuilder newInstance() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationInvalidParamBuilderIetf();
	}

}
