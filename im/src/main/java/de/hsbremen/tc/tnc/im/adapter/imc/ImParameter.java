package de.hsbremen.tc.tnc.im.adapter.imc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.im.module.SupportedMessageType;


public class ImParameter {

	public static final long UNKNOWN_PRIMARY_ID = -1L;
	
	private Set<SupportedMessageType> supportedMessageTypes;
	private boolean tncsFirstSupport;
	private boolean useExclusive;
	private long primaryId;
	
	public ImParameter(){
		this.setPrimaryId(UNKNOWN_PRIMARY_ID);
		this.setTncsFirstSupport(false);
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


	public boolean shouldUseExclusive() {
		return useExclusive;
	}


	public void setUseExclusive(boolean useExclusive) {
		this.useExclusive = useExclusive;
	}

}
