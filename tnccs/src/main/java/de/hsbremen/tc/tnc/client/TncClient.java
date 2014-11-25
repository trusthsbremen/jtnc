package de.hsbremen.tc.tnc.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.im.DefaultImModule;
import de.hsbremen.tc.tnc.im.Im;
import de.hsbremen.tc.tnc.im.loader.ImFileLoader;
import de.hsbremen.tc.tnc.im.manager.ImModuleManager;
import de.hsbremen.tc.tnc.session.TncContext;
import de.hsbremen.tc.tnc.session.context.TncSession;
import de.hsbremen.tc.tnc.session.context.enums.SessionEventEnum;
import de.hsbremen.tc.tnc.transport.TransportFactory;
import de.hsbremen.tc.tnc.transport.connection.TransportAddress;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class TncClient implements TnccsConnector, TncContext{

	// this is very simple and should be changed in the future to make IDs reusable if an IMC is terminated.
	private long imcCounter;
	
	private final Map<String,TransportAddress> peers;
	private final Map<String,TncSession> activeSessions;
	private final List<Im<IMC>> imcList;
	
	private final ExecutorService executor;
	private final ImModuleManager<IMC> imcManager;
	private final TransportFactory transportFactory;
	
	public TncClient(Map<String, TransportAddress> peers, ImModulManager<IMC> imcManager, TransportFactory transportFactory, ExecutorService executor) {

		this.imcCounter = 0;
		
		this.peers = peers;
		this.executor = executor;
		this.imcManager = imcManager;
		this.transportFactory = transportFactory;
		
		this.imcList = new LinkedList<>();
		this.activeSessions = new HashMap<String, TncSession>();
	}

	@Override
	public void initialize() throws NoImIdsLeftException {
		
		imcManager.loadAll(tncConfig);
		for (IMC im : ims) {
			long imcId = this.reserveImId();
			if(imcId >= 0){
				// TODO invoke initialize
				this.imcList.add(new DefaultImModule<IMC>(this.reserveImId(), im));
			}else{
				throw new NoImIdsLeftException("No IMC IDs left, because all IDs are already assigned.",imcCounter);
			}
		}
	}

	@Override
	public void terminate() {
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
		for (Im<IMC> imc : this.imcList) {
			try {
				imc.getIm().terminate();
			} catch (TNCException e) {
				// TODO LOG
				e.printStackTrace();
			}
		}
		this.imcList.clear();
		
		this.imcCounter = 0;
	}

	@Override
	public void requestConnectionHandshake(String peerId) throws ConnectionException {
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
			TransportConnection connection = this.transportFactory.connectTo(this.peers.get(peerId));
			
		}
		
	}

	@Override
	public void requestGlobalHandshake() throws ConnectionException {
		for (String peer: this.peers.keySet()) {
			this.requestConnectionHandshake(peer);
		}
	}
	
//	@Override
//	public void notifyClient(String sessionId, Object updateData) {
//		this.activeSessions.remove(sessionId);
//	}

	@Override
	public long reserveImId() {
		if(imcCounter < TNCConstants.TNC_IMCID_ANY){
			return ++imcCounter;
		}else{
			return -1;
		}
	}

	@Override
	public void addPeer(String peerId, TransportAddress address) {
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

	@Override
	public void notifyClient(String sessionId, SessionEventEnum event,
			Object updateData) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
