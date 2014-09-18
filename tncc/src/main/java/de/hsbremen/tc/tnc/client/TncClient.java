package de.hsbremen.tc.tnc.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.trustedcomputinggroup.tnc.TNCConstants;
import org.trustedcomputinggroup.tnc.TNCException;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.im.container.ImcContainer;
import de.hsbremen.tc.tnc.im.loader.ImLoader;
import de.hsbremen.tc.tnc.session.TncContext;
import de.hsbremen.tc.tnc.session.context.TncSession;
import de.hsbremen.tc.tnc.transport.IfTransportFactory;
import de.hsbremen.tc.tnc.transport.connection.IfTAddress;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class TncClient implements TnccConnector, TncContext{

	private long imcCounter;
	
	private final Map<String,IfTAddress> peers;
	private final Map<String,TncSession> activeSessions;
	private final List<ImcContainer> imcList;
	
	private final ExecutorService executor;
	private final ImLoader<IMC> imcLoader;
	private final IfTransportFactory transportFactory;
	
	public TncClient(Map<String, IfTAddress> peers, ImLoader<IMC> loader, IfTransportFactory transportFactory, ExecutorService executor) {

		this.imcCounter = 0;
		
		this.peers = peers;
		this.executor = executor;
		this.imcLoader = loader;
		this.transportFactory = transportFactory;
		
		this.imcList = new LinkedList<>();
		this.activeSessions = new HashMap<String, TncSession>();
	}

	@Override
	public void startUp() throws NoImIdsLeftException {
		
		List<IMC> ims = imcLoader.loadImlist();
		for (IMC im : ims) {
			long imcId = this.reserveImId();
			if(imcId >= 0){
				this.imcList.add(new ImcContainer(this.reserveImId(), im));
			}else{
				throw new NoImIdsLeftException("No IMC IDs left, because all IDs are already assigned.",Long.toString(imcCounter));
			}
		}
	}

	@Override
	public void shutdown() {
		// stop sessions
		for (Entry<String, TncSession> entry: activeSessions.entrySet()) {
			// always stop session first to avoid unnecessary read/write errors.
			entry.getValue().cancel();
		}
		this.activeSessions.clear();
				
		// shutdown executor
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO LOG
			e.printStackTrace();
		}
		// terminate IMC
		for (ImcContainer imc : this.imcList) {
			try {
				((IMC)imc.getIm()).terminate();
			} catch (TNCException e) {
				// TODO LOG
				e.printStackTrace();
			}
		}
		this.imcList.clear();
		
		this.imcCounter = 0;
	}

	@Override
	public void requestHandshakeWith(String peerId) throws ConnectionException {
		if(this.peers.containsKey(peerId)){
			if(this.activeSessions.containsKey(peerId)){
				this.activeSessions.get(peerId).requestHandshake();
			}else{
				this.createSession(peerId);
			}
		}
	}

	private void createSession(String peerId) throws ConnectionException {
		if(this.peers.containsKey(peerId)){
			IfTConnection connection = this.transportFactory.connectTo(this.peers.get(peerId));
			
		}
		
	}

	@Override
	public void requestHandshakeWithAll() throws ConnectionException {
		for (String peer: this.peers.keySet()) {
			this.requestHandshakeWith(peer);
		}
	}
	
	@Override
	public void notifyClient(String sessionId, Object updateData) {
		this.activeSessions.remove(sessionId);
	}

	@Override
	public long reserveImId() {
		if(imcCounter < TNCConstants.TNC_IMCID_ANY){
			return ++imcCounter;
		}else{
			return -1;
		}
	}

	@Override
	public void addPeer(String peerId, IfTAddress address) {
		this.peers.put(peerId, address);
		
	}

	@Override
	public void removePeer(String peerId) {
		// only removes peer, does not close sessions which are already
		// established with this peer.
		if(this.peers.containsKey(peerId)){
			this.peers.remove(peerId);
		}
	}

	@Override
	public boolean isServer() {
		return false;
	}
	
	
	
}
