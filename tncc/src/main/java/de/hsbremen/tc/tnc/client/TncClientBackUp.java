package de.hsbremen.tc.tnc.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.container.DefaultImContainer;
import de.hsbremen.tc.tnc.im.container.ImContainer;
import de.hsbremen.tc.tnc.im.loader.ImLoader;
import de.hsbremen.tc.tnc.session.context.DefaultPbcSessionBuilder;
import de.hsbremen.tc.tnc.session.context.TncSession;
import de.hsbremen.tc.tnc.session.context.TncSessionBuilder;
import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;

public class TncClientBackUp implements TncContext, TnccConnector {

	
	private long imcCounter;
    private List<DefaultImContainer> imcList;
    private ExecutorService executor;
    private Map<IfTConnection,TncSession> activeSessions;
    private TncSessionBuilder sessionBuilder;
    private ImLoader imcLoader;
    private Map<Long,Object> attributes;
    
    // TODO add reference to connectionHandler with Method establishConnection
    // TODO remove requestHandshake using the IFTConnection
    // TODO add requestHandhake method as trigger only, without parameters
    // TODO what is with more than one TNCS how does the client choose?
    // TODO maybe a get configured TNCS is needed to let the client choose?
    
    public static TncClientBackUp DefaultClient(){
    	// Thread calculation copied from Jetty SelectChannelConnector
    	return new TncClientBackUp(Executors.newFixedThreadPool(Math.max(1, (Runtime.getRuntime().availableProcessors() + 3)/4)), new DefaultPbcSessionBuilder());
    }
    
    public static TncClientBackUp CustomClient(ExecutorService executor, TncSessionBuilder sessionBuilder, ImLoader<IMC> imcLoader){
    	return new TncClientBackUp(executor, sessionBuilder, imcLoader);
    }
   
    private TncClientBackUp(ExecutorService executor, TncSessionBuilder sessionBuilder, ImLoader<IMC> imcLoader){
    	this.executor = executor;
    	this.sessionBuilder = sessionBuilder;
    	this.imcLoader = imcLoader;
    	this.activeSessions = new HashMap<>();
    	this.imcList = new ArrayList<>();
    	this.imcCounter = 0;
    }

	@Override
	public void requestHandshake(IfTConnection connection) {
		if(this.activeSessions.containsKey(connection)){
			this.activeSessions.get(connection).requestHandshake();
		}else{
			this.createSession(connection);
		}
	}
	
	@Override
	public void shutdown() {
		// stop sessions and close connections
		for (Entry<IfTConnection, TncSession> entry: activeSessions.entrySet()) {
			// always stop session first to avoid unnecessary read/write errors.
			entry.getValue().cancel();
			entry.getKey().close();
		}
		this.activeSessions = new HashMap<>();
		
		// shutdown executor
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO LOG
			e.printStackTrace();
		}
		// terminate IMC
		for (ImContainer imc : imcList) {
			try {
				((IMC)imc.getIm()).terminate();
			} catch (TNCException e) {
				// TODO LOG
				e.printStackTrace();
			}
		}
		this.imcList = new ArrayList<>();
		this.imcCounter = 0;
		
		
	}

	@Override
	public void startUp() throws NoImIdsLeftException {
		String path = ""; // TODO where is the path;
		List<IMC> ims = imcLoader.loadImlist();
		for (IMC im : ims) {
			if(im instanceof IMC){
				if(imcCounter < TNCConstants.TNC_IMCID_ANY){
					this.imcList.add(new DefaultImContainer(++imcCounter, im));
				}else{
					throw new NoImIdsLeftException("No IMC IDs left, because all IDs are already assigned.",Long.toString(imcCounter));
				}
			}else{
				throw new NoImIdsLeftException("IM is not of type IMC and cannot be loaded as part of a TNCC.", im.getClass().getCanonicalName());
			}
		}
	}
	
	private void createSession(IfTConnection connection){
		this.sessionBuilder.setConnection(connection);
		this.sessionBuilder.setTncClient(this);
		// TODO one message handler for all (member variable of this class) or a builder to build a separate message handler 
		// per session?
		this.sessionBuilder.setMessageHandler();
		
		TncSession session = this.sessionBuilder.toSession();
		this.activeSessions.put(connection, session);
	}
		
	@Override
	public void notifySessionClosed(IfTConnection connection) {
		TncSession session = this.activeSessions.get(connection);
		session.cancel();
		connection.close();
		this.activeSessions.remove(connection);
	}

	

	
}
