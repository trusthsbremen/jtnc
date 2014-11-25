package org.ietf.nea.pt.connection;

import java.util.HashMap;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.connection.TransportConnectionBuilder;

public class PlainTransportConnectionBuilder implements TransportConnectionBuilder {

	private TnccsReader<TnccsBatch> reader;
	private TnccsWriter<TnccsBatch> writer;
	
	private NetworkTransportAddress address = new NetworkTransportAddress("localhost", 10271);
	private Map<Long, Object> attributes;

	
	public PlainTransportConnectionBuilder(){
		this.attributes = new HashMap<>();
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IFT_PROTOCOL, HSBConstants.HSB_PEN_VENDORID+" IF-T PLAIN");
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_IFT_VERSION, "0.1");
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE, IETFConstants.IETF_MAX_LENGTH);
		this.attributes.put(AttributeSupport.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS, 0xFFFFFFFFL);
	}
	
	public TransportConnectionBuilder setReader(final TnccsReader<TnccsBatch> reader){
		this.reader = reader;
		return this;
	}
	
	public TransportConnectionBuilder setWriter(final TnccsWriter<TnccsBatch> writer){
		this.writer = writer;
		return this;
	}
	
	public TransportConnectionBuilder setDestination(final TransportAddress destination){
		if(destination == null){
			throw new NullPointerException("Address cannot be null.");
		}
		
		if(destination instanceof NetworkTransportAddress){
			this.address = (NetworkTransportAddress)destination;
		}else{
			throw new IllegalArgumentException("Destination must be of type "+ NetworkTransportAddress.class.getCanonicalName() + ".");
		}
		return this;
	}
	
	@Override
	public TransportConnection toConnection() {
		if(this.reader == null){
			throw new IllegalStateException("Reader must be set.");
		}
		
		if(this.writer == null){
			throw new IllegalStateException("Writer must be set.");
		}
		

		return new PlainClientTransportConnection(this.address, this.reader, this.writer, this.attributes);
	}

}
