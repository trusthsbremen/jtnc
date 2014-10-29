package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.message.PaMessageHeaderBuilderIetf;

public class PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf implements
		PaAttributeValueErrorInformationUnsupportedVersionBuilder {
	
	
	private long length;
	private PaMessageHeader messageHeader;
	private short maxVersion;
	private short minVersion;
	
	public PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.ERR_INF.length() + PaAttributeTlvFixedLength.MESSAGE.length() + 4; // 4 = min + max version
		this.messageHeader = new PaMessageHeaderBuilderIetf().toMessageHeader();
		this.minVersion = 0;
		this.maxVersion = 0;
	}

	@Override
	public void setMessageHeader(PaMessageHeader messageHeader) {
		if(messageHeader != null){
			this.messageHeader = messageHeader;
		}
	}

	@Override
	public void setMaxVersion(short maxVersion) {
		this.maxVersion = maxVersion;
	}

	@Override
	public void setMinVersion(short minVersion) {
		this.minVersion = minVersion;
		
	}
	
	@Override
	public PaAttributeValueErrorInformationUnsupportedVersion toValue(){
		
		return new PaAttributeValueErrorInformationUnsupportedVersion(this.length, this.messageHeader, this.maxVersion, this.minVersion);
	}

	@Override
	public PaAttributeValueErrorInformationUnsupportedVersionBuilder clear() {
		// TODO Auto-generated method stub
		return new PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf();
	}

}
