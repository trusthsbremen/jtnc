package org.ietf.nea.pt.connection;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.IfTransportFactory;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class PlainIfTransportFactory implements IfTransportFactory{
	
	private TnccsReader<TnccsBatch> reader;
	private TnccsWriter<TnccsBatch> writer;

	public PlainIfTransportFactory(TnccsReader<TnccsBatch> reader, TnccsWriter<TnccsBatch> writer){
		this.reader = reader;
		this.writer = writer;
	}
	
	@Override
	public IfTConnection connectTo(IfTAddress destination) throws ConnectionException {
		if(!(destination instanceof NetworkIfTAddress)){
			throw new IllegalArgumentException("Destination must be of type "+ NetworkIfTAddress.class.getCanonicalName() + ".");
		}
		PlainIfTConnectionBuilder builder = new PlainIfTConnectionBuilder();
		builder.setReader(this.reader);
		builder.setWriter(this.writer);
		builder.setDestination((NetworkIfTAddress) destination);
		
		IfTConnection connection = builder.toConnection();
		connection.open();
		
		return connection;
	}

	
}
