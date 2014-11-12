package de.hsbremen.tc.tnc.im.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;


public class ImParameter {
	
	private Set<SupportedMessageType> supportedMessageTypes;
	private boolean tncsFirstSupport; 
	private long primaryId;

	public ImParameter(){
		this(false);
	}	
	
	public ImParameter(boolean tncsFirstSupport){
		this.setPrimaryId(HSBConstants.HSB_IM_ID_UNKNOWN);
		this.setTncsFirstSupport(tncsFirstSupport);
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

}
