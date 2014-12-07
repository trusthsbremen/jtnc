package de.hsbremen.tc.tnc.tnccs.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionBase;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultSession;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public class DefaultClient implements Client{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSession.class);
	
	private static final long DEFAULT_SESSION_CLEANUP_INTERVAL = 5000; 
	
	private final Map<TransportConnection,SessionBase> runningSessions;
	private final SessionFactory sessionFactory;
	private boolean started;
	private final long sessionCleanUpInterval;
	
	private Thread sessionCleaner;
	
	public DefaultClient(SessionFactory factory, long sessionCleanUpInterval){

		if(factory != null){
			this.sessionFactory = factory;
		}else{
			throw new NullPointerException("SessionFactory cannot be null.");
		}

		this.sessionCleanUpInterval = (sessionCleanUpInterval <= 0)? DEFAULT_SESSION_CLEANUP_INTERVAL : sessionCleanUpInterval;

		this.runningSessions = new ConcurrentHashMap<>();
		this.started = false;

	}
	
	@Override
	public void notifyConnectionChange(TransportConnection connection,
			ConnectionChangeTypeEnum change) {
		
		if(!started){
			throw new IllegalStateException("Client was not started.");
		}
		
		switch(change){
		case CLOSED:
			this.closeSession(connection);
			break;
		case NEW:
			this.createSession(connection);
			break;
		default:
			LOGGER.warn("Type " + change.toString() + " not implemented.");
		}
		
	}

	private void createSession(TransportConnection connection) {
		if(this.runningSessions.containsKey(connection)){
			this.closeSession(connection);
		}
		
		this.runningSessions.put(connection, this.sessionFactory.createTnccsSession(connection));
		
	}

	private void closeSession(TransportConnection connection) {
		if(this.runningSessions.containsKey(connection)){
			
			if(!this.runningSessions.get(connection).isClosed()){
				this.runningSessions.get(connection).close();
			}
			this.runningSessions.remove(connection);
			
		}else{
			LOGGER.debug("No session found for connection " + connection.toString() + ".");
		}
	}

	
	@Override
	public void notifyGlobalConnectionChange(ConnectionChangeTypeEnum change) {
		
		if(!started){
			throw new IllegalStateException("Client was not started.");
		}
		
		Set<TransportConnection> keys = new HashSet<>(this.runningSessions.keySet());
		
		for (Iterator<TransportConnection> iter = keys.iterator(); iter.hasNext();) {
			TransportConnection connection = iter.next();
			this.notifyConnectionChange(connection, change);
			iter.remove();
		}
		
		// just to be sure
		if(change.equals(ConnectionChangeTypeEnum.CLOSED)){
			this.runningSessions.clear();
		}
		
	}
	
	public void start(){
		this.started = true;
		
		this.sessionCleaner = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					for(Iterator<Entry<TransportConnection, SessionBase>> iter = runningSessions.entrySet().iterator(); iter.hasNext();){
						Entry<TransportConnection, SessionBase> e = iter.next();
						if(e.getValue().isClosed() || !e.getKey().isOpen()){
							iter.remove();
						}
					}
					try {
						Thread.sleep(sessionCleanUpInterval);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
			}
		});
		
		this.sessionCleaner.start();
	}

	
	@Override
	public void terminate() {
		
		if(!started){
			throw new IllegalStateException("Client was not started.");
		}
		this.notifyGlobalConnectionChange(ConnectionChangeTypeEnum.CLOSED);
		this.sessionCleaner.interrupt();
	}

}
