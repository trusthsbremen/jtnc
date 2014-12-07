package de.hsbremen.tc.tnc.tnccs.session.connection.simple;

import java.io.IOException;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultTnccsOutputChannel implements TnccsOutputChannel{

	private final TransportConnection connection;
	private final TnccsWriter<? super TnccsBatch> writer;

	DefaultTnccsOutputChannel(TransportConnection connection,
			TnccsWriter<? super TnccsBatch> writer) {
		this.connection = connection;
		this.writer = writer;
	}

	@Override
	public void send(TnccsBatch batch) throws ConnectionException, SerializationException {
		
		this.checkConnection();
		this.writer.write(batch, this.connection.getOutputStream());
		try{
			this.connection.getOutputStream().flush();
		}catch(IOException e){
			throw new ConnectionException(e.getMessage(),e);
		}
		
		
	}

	public void close(){
		this.connection.close();
	}
	
	private void checkConnection() throws ConnectionException{
		if(!connection.isOpen()){
			throw new ConnectionException("Underlying connection is closed.");
		}
	}
}
