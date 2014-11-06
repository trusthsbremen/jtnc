package de.hsbremen.tc.tnc.im.adapter.connection;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

public class ImcConnectionAdapterFactoryIetf implements ImcConnectionAdapterFactory {

	private ImWriter<ImMessage> writer;
	
	@SuppressWarnings("unchecked")
	public ImcConnectionAdapterFactoryIetf(ImWriter<? extends ImMessage> writer) {
		if(writer == null){
			throw new NullPointerException("Writer cannot be null.");
		}
		
		this.writer = (ImWriter<ImMessage>) writer;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory#createConnectionAdapter(org.trustedcomputinggroup.tnc.ifimc.IMCConnection)
	 */
	@Override
	public ImcConnectionAdapter createConnectionAdapter(IMCConnection connection) {
		
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}
		
		return new ImcConnectionAdapterIetf(writer, connection);
	}
}
