package de.hsbremen.tc.tnc.transport.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class SocketTransportConnection implements TransportConnection{
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketTransportConnection.class);
	private final Socket socket;
	private TransportAddress address;
	private boolean selfInitiated;
	private Attributed attributes;
	
	public SocketTransportConnection(boolean selfInitiated, Socket socket, Attributed attributes, TransportAddress address){
		this.socket = socket;
		this.selfInitiated = selfInitiated;
		this.attributes = attributes;
		this.address = address;
	}
	
	@Override
	public TransportAddress getAddress() {
		return this.address;
	}

	@Override
	public boolean isSelfInititated() {
		return selfInitiated;
	}

	@Override
	public boolean isOpen() {
		if(socket != null){
			if(!socket.isClosed() && !socket.isInputShutdown() && !socket.isOutputShutdown()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void open() throws ConnectionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		try {
			this.socket.shutdownInput();
		} catch (IOException e) {
			LOGGER.warn("Socket InputStream could not be closed.", e);
		}finally{
			try {
				this.socket.close();
			} catch (IOException e) {
				LOGGER.warn("Socket could not be closed.", e);
			}
		}
		
	}

	@Override
	public OutputStream getOutputStream() throws ConnectionException{
		if(isOpen()){
			try {
				return socket.getOutputStream();
			} catch (IOException e) {
				throw new ConnectionException("Socket OutputStream is not accessible.", e);
			}
		}
		
		throw new ConnectionException("The socket seems not open.");
	}

	@Override
	public InputStream getInputStream() throws ConnectionException{
		if(isOpen()){
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				throw new ConnectionException("Socket InputStream is not accessible.", e);
			}
		}
		
		throw new ConnectionException("The socket seems not open.");
	}

	@Override
	public Attributed getAttributes() {
		return this.attributes;
	}

}
