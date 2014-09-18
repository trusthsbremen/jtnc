package org.ietf.nea.pt.connection;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;
import de.hsbremen.tc.tnc.transport.IfTransportFactory;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class PlainIfTransportFactory implements IfTransportFactory{

	private TnccsSerializer<TnccsBatch> serializer;

	public PlainIfTransportFactory(TnccsSerializer<TnccsBatch> serializer){
		this.serializer = serializer;
	}
	
	@Override
	public IfTConnection connectTo(IfTAddress destination) throws ConnectionException {
		if(!(destination instanceof NetworkIfTAddress)){
			throw new IllegalArgumentException("Destination must be of type "+ NetworkIfTAddress.class.getCanonicalName() + ".");
		}
		PlainIfTConnectionBuilder builder = new PlainIfTConnectionBuilder();
		builder.setSerializer(this.serializer);
		builder.setDestination((NetworkIfTAddress) destination);
		
		IfTConnection connection = builder.toConnection();
		connection.open();
		
		return connection;
	}

	
}
