package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilderIetf;
import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;

public class PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf implements
	PaAttributeValueErrorInformationUnsupportedAttributeBuilder {
	
	
	private long length;
	private RawMessageHeader messageHeader;
	private PaAttributeHeader attributeHeader;

	
	public PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.ERR_INF.length() + 
				PaAttributeTlvFixedLength.MESSAGE.length() + 
				PaAttributeTlvFixedLength.ATTRIBUTE.length() - 4; // -4 = attribute length is ignored
		
		this.messageHeader = new RawMessageHeader((short)0, new byte[0], 0L);
		this.attributeHeader = new PaAttributeHeaderBuilderIetf().toAttributeHeader();
	}

	@Override
	public void setMessageHeader(RawMessageHeader messageHeader) {
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
	public PaAttributeValueErrorInformationUnsupportedAttribute toValue(){
		
		return new PaAttributeValueErrorInformationUnsupportedAttribute(this.length, this.messageHeader, this.attributeHeader);
	}

	@Override
	public PaAttributeValueErrorInformationUnsupportedAttributeBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationUnsupportedAttributeBuilderIetf();
	}

}
