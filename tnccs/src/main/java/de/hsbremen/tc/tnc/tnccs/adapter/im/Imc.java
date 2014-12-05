package de.hsbremen.tc.tnc.tnccs.adapter.im;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class Imc {

	private final long primaryId;
	private final Set<Long> allIds;
	
	private final ImcAdapter adapter;
	
	private Set<SupportedMessageType> supportedMessageTypes;

	public Imc(long primaryId, ImcAdapter adapter) {
		super();
		this.primaryId = primaryId;
		this.allIds = new HashSet<>();
		this.allIds.add(this.primaryId);
		this.adapter = adapter;
		this.supportedMessageTypes = new HashSet<>();
		
	}

	/**
	 * @return the primaryId
	 */
	public long getPrimaryId() {
		return this.primaryId;
	}

	/**
	 * @return the allIds
	 */
	public Set<Long> getAllIds() {
		return Collections.unmodifiableSet(this.allIds);
	}

	/**
	 * @param the id
	 */
	public void addId(long id) {
		this.allIds.add(id);
	}
	
	/**
	 * @return the adapter
	 */
	public ImcAdapter getAdapter() {
		return this.adapter;
	}

	/**
	 * @return the supportedMessageTypes
	 */
	public Set<SupportedMessageType> getSupportedMessageTypes() {
		return this.supportedMessageTypes;
	}

	/**
	 * @param supportedMessageTypes the supportedMessageTypes to set
	 */
	public void setSupportedMessageTypes(
			Set<SupportedMessageType> supportedMessageTypes) {
		this.supportedMessageTypes = supportedMessageTypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.primaryId ^ (this.primaryId >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Imc other = (Imc) obj;
		if (this.primaryId != other.primaryId) {
			return false;
		}
		return true;
	}
	
	
	
}
