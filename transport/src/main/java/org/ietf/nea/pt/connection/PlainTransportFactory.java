package org.ietf.nea.pt.connection;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.connection.TransportFactory;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class PlainTransportFactory implements TransportFactory{
	
	private TnccsReader<TnccsBatch> reader;
	private TnccsWriter<TnccsBatch> writer;

	public PlainTransportFactory(TnccsReader<TnccsBatch> reader, TnccsWriter<TnccsBatch> writer){
		this.reader = reader;
		this.writer = writer;
	}
	
	@Override
	public TransportConnection connectTransporto(TransportAddress destination) throws ConnectionException {
		if(!(destination instanceof NetworkTransportAddress)){
			throw new IllegalArgumentException("Destination must be of type "+ NetworkTransportAddress.class.getCanonicalName() + ".");
		}
		PlainTransportConnectionBuilder builder = new PlainTransportConnectionBuilder();
		builder.setReader(this.reader);
		builder.setWriter(this.writer);
		builder.setDestination((NetworkTransportAddress) destination);
		
		TransportConnection connection = builder.toConnection();
		connection.open();
		
		return connection;
	}

	
}
