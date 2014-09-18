package org.ietf.nea.pt.connection;

import java.util.HashMap;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.connection.IfTConnectionBuilder;

public class PlainIfTConnectionBuilder implements IfTConnectionBuilder {

	private TnccsSerializer<TnccsBatch> serializer;
	private NetworkIfTAddress address = new NetworkIfTAddress("localhost", 10271);
	private Map<Long, Object> attributes;
	
	public PlainIfTConnectionBuilder(){
		this.attributes = new HashMap<>();
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IFT_PROTOCOL, HSBConstants.HSB_PEN_VENDORID+" IF-T PLAIN");
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IFT_VERSION, "0.1");
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE, IETFConstants.IETF_MAX_LENGTH);
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS, 0xFFFFFFFFL);
	}
	
	public IfTConnectionBuilder setSerializer(final TnccsSerializer<TnccsBatch> serializer){
		this.serializer = serializer;
		return this;
	}
	
	public IfTConnectionBuilder setDestination(final IfTAddress destination){
		if(destination != null && destination instanceof NetworkIfTAddress){
			this.address = (NetworkIfTAddress)destination;
		}
		return this;
	}
	
	@Override
	public IfTConnection toConnection() {
		if(this.serializer == null){
			throw new IllegalStateException("Serializer must be set first.");
		}

		String input = this.address.getHost() + ":" + this.address.getPort();

		return new PlainIfTConnection(this.address, input, this.serializer, this.attributes);
	}

}
