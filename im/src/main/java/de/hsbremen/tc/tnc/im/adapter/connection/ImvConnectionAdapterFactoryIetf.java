package de.hsbremen.tc.tnc.im.adapter.connection;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.m.message.ImMessage;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

public class ImvConnectionAdapterFactoryIetf implements ImvConnectionAdapterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImvConnectionAdapterFactory.class);
	private ImWriter<ImMessage> writer;
	
	@SuppressWarnings("unchecked")
	public ImvConnectionAdapterFactoryIetf(ImWriter<? extends ImMessage> writer) {
		if(writer == null){
			throw new NullPointerException("Writer cannot be null.");
		}
		
		this.writer = (ImWriter<ImMessage>) writer;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactory#createConnectionAdapter(org.trustedcomputinggroup.tnc.ifimc.IMCConnection)
	 */
	@Override
	public ImvConnectionAdapter createConnectionAdapter(IMVConnection connection) {
		
		if(connection == null){
			throw new NullPointerException("Connection cannot be null.");
		}

		
		ImvConnectionAdapter adapter = new ImvConnectionAdapterIetf(writer, connection);
		
		if(LOGGER.isDebugEnabled()){
			this.writeConnectionInformationToDebugLog(adapter);
		}
		
		return adapter;
	}
	
	private void writeConnectionInformationToDebugLog(ImvConnectionAdapter connection) {
		StringBuilder b = new StringBuilder();
		b.append("Create session with connection ")
		.append(connection.toString())
		.append(".\n");

		b.append("The following parameters are set and accessible:\n");
		List<TncAttributeType> clientTypes = DefaultTncAttributeTypeFactory.getInstance().getServerTypes();
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
