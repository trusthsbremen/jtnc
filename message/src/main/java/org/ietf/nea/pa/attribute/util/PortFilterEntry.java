package org.ietf.nea.pa.attribute.util;

import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;

public class PortFilterEntry {
	
	private final PaAttributePortFilterStatus blocked; 		// 1 bit(s)
	private final short protocolNumber; // 8 bit(s)
	private final int portNumber;		// 16 bit(s)
	
	public PortFilterEntry(PaAttributePortFilterStatus blocked, short protocolNumber,
			int portNumber) {
		
		this.blocked = blocked; 
		this.protocolNumber = protocolNumber;
		this.portNumber = portNumber;
	}

	/**
	 * @return the bFlag
	 */
	public PaAttributePortFilterStatus getFilterStatus() {
		return this.blocked;
	}

	/**
	 * @return the protocolNumber
	 */
	public short getProtocolNumber() {
		return this.protocolNumber;
	}

	/**
	 * @return the portNumber
	 */
	public int getPortNumber() {
		return this.portNumber;
	}
}
