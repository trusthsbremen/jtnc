package de.hsbremen.tc.tnc.session.connection;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class DefaultBatchSender implements BatchSender{

	private TnccsOutputChannel channel;
	
	@Override
	public void register(TnccsOutputChannel channel){
		this.channel  = channel;
	}
	
	@Override
	public void send(TnccsBatch batch) throws ComprehensibleException{
		try {
			this.channel.send(batch);
		} catch (ConnectionException | SerializationException e) {
			throw e;
		}
	}
	
	public void close(){
		this.channel.close();
	}
	
}
