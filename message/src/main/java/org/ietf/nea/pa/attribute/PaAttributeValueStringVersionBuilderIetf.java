package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.validate.rules.NoNullTerminatedString;

public class PaAttributeValueStringVersionBuilderIetf implements PaAttributeValueStringVersionBuilder{

	private long length;
	private String productVersion;
	private String buildVersion;
	private String configVersion;
	
	public PaAttributeValueStringVersionBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.STR_VER.length();
		this.productVersion = "";
		this.buildVersion = "";
		this.configVersion = "";
	}
	
	@Override
	public void setProductVersion(String productVersion) throws RuleException {
		if(productVersion != null){
			NoNullTerminatedString.check(productVersion);
			this.productVersion = productVersion;
			this.updateLength();
		}
		
	}

	@Override
	public void setBuildNumber(String buildNumber) throws RuleException {
		if(buildNumber != null){
			NoNullTerminatedString.check(buildNumber);
			this.buildVersion = buildNumber;
			this.updateLength();
		}
		
	}

	@Override
	public void setConfigurationVersion(String configVersion)
			throws RuleException {
		if(configVersion != null){
			NoNullTerminatedString.check(configVersion);
			this.configVersion = configVersion;
			this.updateLength();
		}
		
	}
	
	@Override
	public PaAttributeValueStringVersion toValue() {
		return new PaAttributeValueStringVersion(this.length, this.productVersion, this.buildVersion, this.configVersion);
	}

	@Override
	public PaAttributeValueStringVersionBuilder clear() {
		return new PaAttributeValueStringVersionBuilderIetf();
	}
	
	private void updateLength(){
		this.length = PaAttributeTlvFixedLength.STR_VER.length();
		if(productVersion.length() > 0){
			this.length += productVersion.getBytes(Charset.forName("UTF-8")).length;
		}
		
		if(buildVersion.length() > 0){
			this.length += buildVersion.getBytes(Charset.forName("UTF-8")).length;
		}
		
		if(configVersion.length() > 0){
			this.length += configVersion.getBytes(Charset.forName("UTF-8")).length;
		}
	}

}
