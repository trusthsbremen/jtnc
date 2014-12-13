package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.RawMessageHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf implements
		PaAttributeValueErrorInformationUnsupportedVersionBuilder {
	
	
	private long length;
	private RawMessageHeader messageHeader;
	private short maxVersion;
	private short minVersion;
	
	public PaAttributeValueErrorInformationUnsupportedVersionBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.ERR_INF.length() + PaAttributeTlvFixedLengthEnum.MESSAGE.length() + 4; // 4 = min + max version
		this.messageHeader = new RawMessageHeader((short)0, new byte[0], 0L);
		this.minVersion = 0;
		this.maxVersion = 0;
	}

	@Override
	public void setMessageHeader(RawMessageHeader messageHeader) {
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
