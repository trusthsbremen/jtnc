package org.ietf.nea.pt.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class PlainClientTransportConnection extends AbstractTransportConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlainClientTransportConnection.class);
	private Socket socket;
	private final NetworkTransportAddress address;
	
	PlainClientTransportConnection(final TransportAddress address,
			final TnccsReader<TnccsBatch> reader, TnccsWriter<TnccsBatch> writer, final Map<Long,Object> attributes) {
		super(address, reader, writer, attributes /*, selfInitated*/);
	        
		this.address = (NetworkTransportAddress)address;
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
		try {
			this.socket = new Socket(this.address.getHost(), this.address.getPort());
		} catch (UnknownHostException e) {
			throw new ConnectionException("Host is unknown or could not be resolved.",e,this.address.getHost(), this.address.getPort());
		} catch (IOException e) {
			throw new ConnectionException("IO error occured during socket connect.",e);
		}
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
	protected OutputStream getOutputStream() throws ConnectionException{
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
	protected InputStream getInputStream() throws ConnectionException{
		if(isOpen()){
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				throw new ConnectionException("Socket InputStream is not accessible.", e);
			}
		}
		
		throw new ConnectionException("The socket seems not open.");
	}

}
