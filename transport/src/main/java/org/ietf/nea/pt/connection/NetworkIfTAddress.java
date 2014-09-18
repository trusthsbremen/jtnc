package org.ietf.nea.pt.connection;

import de.hsbremen.tc.tnc.transport.connection.IfTAddress;

public class NetworkIfTAddress implements IfTAddress{

	private final String host;
	private final int port; 
	
	public NetworkIfTAddress(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

}
