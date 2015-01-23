package org.ietf.nea.pt.socket;

import java.net.Socket;
import java.util.concurrent.Executors;

import org.ietf.nea.pt.DefaultTransportAttributes;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.transport.TransportAddress;
import de.hsbremen.tc.tnc.transport.TransportConnection;
import de.hsbremen.tc.tnc.transport.TransportConnectionBuilder;

public class SocketTransportConnectionBuilder implements TransportConnectionBuilder{

	private final String transportProtocolId;
	private final String transportProtocolVersion;
	
	private final TransportWriter<TransportMessage> writer;
	private final TransportReader<TransportMessageContainer> reader;
	
	private final long memoryBoarder;

	private long messageLength;
	private long imMessageLength;
	private long maxRoundTrips;
	
	
	
	public SocketTransportConnectionBuilder(
			String transportProtocolId, 
			String transportProtocolVersion, 
			TransportWriter<TransportMessage> writer, 
			TransportReader<TransportMessageContainer> reader){
		
		this.transportProtocolId = transportProtocolId;
		this.transportProtocolVersion = transportProtocolVersion;
		
		this.writer = writer;
		this.reader = reader;
		
		this.memoryBoarder = Runtime.getRuntime().totalMemory();
		
		this.messageLength = (memoryBoarder/2);
		this.imMessageLength = (memoryBoarder/10);
		this.maxRoundTrips = HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN;
		
	}

	/**
	 * @return the messageLength
	 */
	public long getMessageLength() {
		return this.messageLength;
	}

	/**
	 * @return the imMessageLength
	 */
	public long getImMessageLength() {
		return this.imMessageLength;
	}

	/**
	 * @return the maxRoundTrips
	 */
	public long getMaxRoundTrips() {
		return this.maxRoundTrips;
	}

	/**
	 * @return the memoryBoarder
	 */
	public long getMemoryBoarder() {
		return this.memoryBoarder;
	}

	/**
	 * @param messageLength the messageLength to set
	 */
	public SocketTransportConnectionBuilder setMessageLength(long messageLength) {
		if(messageLength <= 0 || messageLength > this.memoryBoarder){
			throw new IllegalArgumentException("Message length is not acceptable, it must be between 1 and " + this.memoryBoarder + " bytes.");
		}
		this.messageLength = messageLength;
		
		return this;
	}

	/**
	 * @param imMessageLength the imMessageLength to set
	 */
	public SocketTransportConnectionBuilder setImMessageLength(long imMessageLength) {
		if(imMessageLength <= 0 || imMessageLength > this.memoryBoarder){
			throw new IllegalArgumentException("Message length is not acceptable, it must be between 1 and " + this.memoryBoarder + " bytes.");
		}
		this.imMessageLength = imMessageLength;
		
		return this;
	}

	/**
	 * @param maxRoundTrips the maxRoundTrips to set
	 */
	public SocketTransportConnectionBuilder setMaxRoundTrips(long maxRoundTrips) {
		if(maxRoundTrips <= 0){
			throw new IllegalArgumentException("Round trips cannot be null.");
		}
		this.maxRoundTrips = maxRoundTrips;
		
		return this;
	}

	@Override
	public TransportConnection toConnection(boolean selfInitiated, boolean server, Object underlying) {
		
		if(underlying == null){
			throw new NullPointerException("Underlying cannot be NULL.");
		}
		
		if(!(underlying instanceof Socket)){
			throw new IllegalArgumentException("Underlying must bve of type " + Socket.class.getCanonicalName() +".");
		}

		Socket socket = (Socket)underlying;
		
		TransportAddress address = new SocketTransportAddress(socket.getInetAddress().getHostName(), socket.getPort());
		
		DefaultTransportAttributes attributes = new DefaultTransportAttributes(transportProtocolId, transportProtocolVersion,this.messageLength,this.imMessageLength,this.maxRoundTrips);
		
		SocketTransportConnection t = new SocketTransportConnection(selfInitiated,server,socket,attributes, address,writer,reader, Executors.newSingleThreadExecutor());
		
		return t;
	}
	
	
	

}
