package de.hsbremen.tc.tnc.m.message;

public interface ImMessageHeader {
	
	/* 1. must be 1 for IETF compliant version
	 * 2. Version not Supported Error in PA-TNC message (no additional messages allowed)
	 * 
	 */
	public short getVersion();
	
	/*
	 * reserved = 0 25 bits
	 */
	
	/*
	 * 1. Unique Value during the assesement (random or sequenze) (uniqueness my be checked)
	 * 2. Inlcude in error message so the party nows wich message was errornouse
	 */
	public long getIdentifier();
	
}
