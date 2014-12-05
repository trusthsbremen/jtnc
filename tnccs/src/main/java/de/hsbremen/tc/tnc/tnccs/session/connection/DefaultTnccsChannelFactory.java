package de.hsbremen.tc.tnc.tnccs.session.connection;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public class DefaultTnccsChannelFactory implements TnccsChannelFactory{
	
	private final TnccsReader<? extends TnccsBatchContainer> reader;
	private final TnccsWriter<? super TnccsBatch> writer;
	
	private final String protocol;
	private final String version;
	
	public DefaultTnccsChannelFactory(String protocol, String version,
			TnccsReader<? extends TnccsBatchContainer> reader,
			TnccsWriter<? super TnccsBatch> writer) {

		this.protocol = protocol;
		this.version = version;
		this.reader = reader;
		this.writer = writer;
	}
	
	@Override
	public String getProtocol() {
		return this.protocol;
	}

	@Override
	public String getVersion() {
		return this.version;
	}
	
	
	@Override
	public TnccsInputChannel createInputChannel(TransportConnection connection,
			TnccsInputChannelListener listener) {
		
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}
		
		if(listener == null){
			throw new NullPointerException("Listener cannot be null.");
		}
		
		TnccsInputChannel inChannel = new DefaultTnccsInputChannel(connection, reader);
		inChannel.register(listener);
		
		return inChannel;
	}
	@Override
	public TnccsOutputChannel createOutputChannel(TransportConnection connection) {
		
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}
		
		TnccsOutputChannel outChannel = new DefaultTnccsOutputChannel(connection, writer); 
		
		return outChannel;
	}	
}
