package de.hsbremen.tc.tnc.transport.example;

import java.net.Socket;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.t.enums.TcgTProtocolEnum;
import de.hsbremen.tc.tnc.message.t.enums.TcgTVersionEnum;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.connection.TransportConnectionBuilder;

public class SocketTransportConnectionBuilder implements TransportConnectionBuilder{

	private Socket socket;
	
	public SocketTransportConnectionBuilder(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public TransportConnection toConnection(boolean selfInitiated) {
		TransportAddress address = new SocketTransportAddress(socket.getInetAddress().getHostName(), socket.getPort());
		Attributed attributes = new DefaultTransportAttributes(TcgTProtocolEnum.PLAIN.value(), TcgTVersionEnum.V1.value());
		SocketTransportConnection t = new SocketTransportConnection(selfInitiated, socket, attributes, address);
		return t;
	}
	
	
	

}
