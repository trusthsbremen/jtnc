package de.hsbremen.tc.tnc.im.adapter.connection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

public class ImcConnectionAdapterFactoryIetf implements ImcConnectionAdapterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImcConnectionAdapterFactory.class);
	private static final short IF_M_VERSION = 1;
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
		
		ImcConnectionAdapter adapter = new ImcConnectionAdapterIetf(IF_M_VERSION, writer, connection);
		
		if(LOGGER.isDebugEnabled()){
			this.writeConnectionInformationToDebugLog(adapter);
		}
		
		return adapter;
	}
	
	private void writeConnectionInformationToDebugLog(ImcConnectionAdapter connection) {
		StringBuilder b = new StringBuilder();
		b.append("Create session with connection ")
		.append(connection.toString())
		.append(".\n");
		
		b.append("The following parameters are set and accessible:\n");
		List<TncAttributeType> clientTypes = DefaultTncAttributeTypeFactory.getInstance().getClientTypes();
		try{
			for (TncAttributeType tncAttributeType : clientTypes) {
				
				try{
					Object o = connection.getAttribute(tncAttributeType);
					if( o != null ){
						b.append(tncAttributeType.toString() + ": ");
						b.append(o.toString()).append("\n");
					}
				}catch(TncException e){
					b.append("Not accessible. Reason: ").append(e.getResultCode().toString()).append("\n");
				}
			}
		}catch(UnsupportedOperationException e){
			b.append("Connection does not support parameter access.");
		}
		
		LOGGER.debug(b.toString());
	}
	
}
