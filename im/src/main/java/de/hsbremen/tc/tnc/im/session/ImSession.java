package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.im.connection.ImConnection;
import de.hsbremen.tc.tnc.im.container.ImContainer;

public class ImSession {
	private final ImContainer im;
	private final ImConnection connection;

	public ImSession(ImContainer im, ImConnection connection) {
		this.im = im;
		this.connection = connection;
	}

	/**
	 * @return the im
	 */
	public ImContainer getIm() {
		return this.im;
	}
	
	/**
	 * @return the connection
	 */
	public ImConnection getConnection() {
		return this.connection;
	}

}

