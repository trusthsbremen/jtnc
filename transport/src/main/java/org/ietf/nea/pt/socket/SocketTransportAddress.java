package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.transport.TransportAddress;


public class SocketTransportAddress implements TransportAddress{

	private final String host;
	private final int port; 
	
	public SocketTransportAddress(String host, int port){
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SocketTransportAddress [host=" + this.host + ", port="
				+ this.port + "]";
	}

	
}
