package org.ietf.nea.pt.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class PlainIfTConnection extends AbstractIfTConnection {

	private Socket socket;
	private final NetworkIfTAddress address;
	
	PlainIfTConnection(final IfTAddress address, final String connectionId,
			final TnccsReader<TnccsBatch> reader, TnccsWriter<TnccsBatch> writer, final Map<Long,Object> attributes) {
		super(connectionId, reader, writer, attributes /*, selfInitated*/);
	        
		this.address = (NetworkIfTAddress)address;
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
			this.socket.close();
		} catch (IOException e) {
			// TODO LOG but since it should be closed do nothing else.
			e.printStackTrace();
		}

	}

	@Override
	protected OutputStream getOutputStream() {
		if(isOpen()){
			try {
				return socket.getOutputStream();
			} catch (IOException e) {
				// TODO LOG and return null
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected InputStream getInputStream() {
		if(isOpen()){
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				// TODO LOG and return null
				e.printStackTrace();
			}
		}
		return null;
	}

}
