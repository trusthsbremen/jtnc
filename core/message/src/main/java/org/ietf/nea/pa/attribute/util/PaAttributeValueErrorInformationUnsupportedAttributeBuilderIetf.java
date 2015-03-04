package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilderIetf;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf implements
	PaAttributeValueErrorInformationUnsupportedAttributeBuilder {
	
	
	private long length;
	private MessageHeaderDump messageHeader;
	private PaAttributeHeader attributeHeader;

	
	public PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length() + 
				PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 
				PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length() - 4; // -4 = attribute length is ignored
		
		this.messageHeader = new MessageHeaderDump((short)0, new byte[0], 0L);
		this.attributeHeader = new PaAttributeHeaderBuilderIetf().toObject();
	}

	@Override
	public void setMessageHeader(MessageHeaderDump messageHeader) {
		if(messageHeader != null){
			this.messageHeader = messageHeader;
		}
	}

	@Override
	public void setAttributeHeader(PaAttributeHeader attributeHeader) {
		if(attributeHeader != null){
			this.attributeHeader = attributeHeader;
		}
	}

	@Override
	public PaAttributeValueErrorInformationUnsupportedAttribute toObject(){
		
		return new PaAttributeValueErrorInformationUnsupportedAttribute(this.length, this.messageHeader, this.attributeHeader);
	}

	@Override
	public PaAttributeValueErrorInformationUnsupportedAttributeBuilder newInstance() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf();
	}

}
