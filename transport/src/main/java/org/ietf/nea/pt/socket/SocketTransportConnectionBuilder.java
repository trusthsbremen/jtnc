package org.ietf.nea.pt.socket;

import java.net.Socket;
import java.util.concurrent.Executors;

import org.ietf.nea.pt.DefaultTransportAttributes;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.message.t.serialize.TransportMessageContainer;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportReader;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
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
	 * @return the transportProtocolId
	 */
	public String getTransportProtocolId() {
		return this.transportProtocolId;
	}



	/**
	 * @return the transportProtocolVersion
	 */
	public String getTransportProtocolVersion() {
		return this.transportProtocolVersion;
	}



	/**
	 * @return the writer
	 */
	public TransportWriter<TransportMessage> getWriter() {
		return this.writer;
	}



	/**
	 * @return the reader
	 */
	public TransportReader<TransportMessageContainer> getReader() {
		return this.reader;
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

	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.transport.TransportConnectionBuilder#toConnection(java.lang.String, boolean, boolean, java.lang.Object)
	 */
	@Override
	public TransportConnection toConnection(String id, boolean selfInitiated,
			boolean server, Object underlying) {
		
		if(underlying == null){
			throw new NullPointerException("Underlying cannot be NULL.");
		}
		
		if(!(underlying instanceof Socket)){
			throw new IllegalArgumentException("Underlying must be of type " + Socket.class.getCanonicalName() +".");
		}

		Socket socket = (Socket)underlying;
		
		DefaultTransportAttributes attributes = new DefaultTransportAttributes(id,this.transportProtocolId, this.transportProtocolVersion,this.messageLength,this.imMessageLength,this.maxRoundTrips);
		
		SocketTransportConnection t = new SocketTransportConnection(selfInitiated,server,socket,attributes, writer,reader, Executors.newSingleThreadExecutor());
		
		return t;
	}



	@Override
	public TransportConnection toConnection(boolean selfInitiated, boolean server, Object underlying) {
		
		if(underlying == null){
			throw new NullPointerException("Underlying cannot be NULL.");
		}
		
		if(!(underlying instanceof Socket)){
			throw new IllegalArgumentException("Underlying must be of type " + Socket.class.getCanonicalName() +".");
		}

		Socket socket = (Socket)underlying;
		
		String id = socket.getInetAddress().getHostAddress();
		
		return this.toConnection(id,selfInitiated, server, underlying);
				
	}
	
	
	

}
