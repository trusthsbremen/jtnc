package de.hsbremen.tc.tnc.im.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.report.SupportedMessageType;


public class ImParameter{
	
	private Set<SupportedMessageType> supportedMessageTypes;
	private boolean tncsFirstSupport; 
	private long primaryId;
	private String preferredLanguage;	
	
	public ImParameter(){
		this(HSBConstants.HSB_IM_ID_UNKNOWN,false, "Accept-Language: en");
	}
	
	public ImParameter(boolean tncsFirst){
		this(HSBConstants.HSB_IM_ID_UNKNOWN,tncsFirst, "Accept-Language: en");
	}
	
	public ImParameter(long primaryId, boolean tncsFirstSupport, String preferredLanguage){
		this.setPrimaryId(primaryId);
		this.setTncsFirstSupport(tncsFirstSupport);
		this.setPreferredLanguage(preferredLanguage);
		this.supportedMessageTypes = new HashSet<>();
		
	}
	
	public boolean hasTncsFirstSupport() {
		return this.tncsFirstSupport;
	}

	public void setTncsFirstSupport(boolean supportsTncsFirst) {
		this.tncsFirstSupport = supportsTncsFirst;
	}

	public long getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(long primaryId) {
		this.primaryId = primaryId;
	}

	public Set<SupportedMessageType> getSupportedMessageTypes() {
		return Collections.unmodifiableSet(supportedMessageTypes);
	}

	public void setSupportedMessageTypes(Set<SupportedMessageType> supportedMessageTypes) {
		this.supportedMessageTypes = supportedMessageTypes;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getPreferredLanguage() {
		return this.preferredLanguage;
	}
	
}
