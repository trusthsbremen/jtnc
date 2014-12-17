package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

public class PaAttributeValueNumericVersionBuilderIetf implements
	PaAttributeValueNumericVersionBuilder {

	private long length;
	private long majorVersion;
	private long minorVersion;
	private long buildVersion;
	private int servicePackMajor;
	private int servicePackMinor;
	
	public PaAttributeValueNumericVersionBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.NUM_VER.length();
		this.majorVersion = 0L;
		this.minorVersion = 0L;
		this.buildVersion = 0L;
		this.servicePackMajor = 0;
		this.servicePackMinor = 0;
	}

	@Override
	public void setMajorVersion(long majorVersion) {
		this.majorVersion = majorVersion;
	}

	@Override
	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
	}

	@Override
	public void setBuildVersion(long buildVersion) {
		this.buildVersion = buildVersion;
	}

	@Override
	public void setServicePackMajor(int servicePackMajor) {
		this.servicePackMajor = servicePackMajor;
	}

	@Override
	public void setServicePackMinor(int servicePackMinor) {
		this.servicePackMinor = servicePackMinor;
	}

	@Override
	public PaAttributeValueNumericVersion toObject(){
		
		return new PaAttributeValueNumericVersion(this.length, this.majorVersion, this.minorVersion, this.buildVersion, this.servicePackMajor, this.servicePackMinor);
	}

	@Override
	public PaAttributeValueNumericVersionBuilder newInstance() {
		return new PaAttributeValueNumericVersionBuilderIetf();
	}

}
