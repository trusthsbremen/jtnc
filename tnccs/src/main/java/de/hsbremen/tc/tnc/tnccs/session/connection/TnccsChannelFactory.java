package de.hsbremen.tc.tnc.tnccs.session.connection;

import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface TnccsChannelFactory {
	
	public String getProtocol();
	public String getVersion();

	public TnccsInputChannel createInputChannel(TransportConnection connection, TnccsInputChannelListener listener);
	public TnccsOutputChannel createOutputChannel(TransportConnection connection);
}
