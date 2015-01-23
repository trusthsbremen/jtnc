package de.hsbremen.tc.tnc.im.session;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;

public class DefaultImSessionManager<K,V extends ImSession> implements ImSessionManager<K, V> {
	
	public static final long DEFAULT_CLEANUP_INTERVAL = 3000;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImSessionManager.class);
	private final Map<K, V> runningSessions;
	private final long sessionCleanUpInterval;
	private ExecutorService service;

	public DefaultImSessionManager(){
		this(DEFAULT_CLEANUP_INTERVAL);
	}
	
	public DefaultImSessionManager(long sessionCleanUpInterval) {
		this.runningSessions = new ConcurrentHashMap<>();
		this.sessionCleanUpInterval = sessionCleanUpInterval;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.SessionManager#getSession(K)
	 */
	@Override
	public V getSession(K connection){
		return this.runningSessions.get(connection);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.SessionManager#putSession(K, V)
	 */
	@Override
	public V putSession(K connection, V session){
		LOGGER.debug("New session "+ session.toString() +" for connection "+ connection.toString() +" added.");
		return this.runningSessions.put(connection, session);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.SessionManager#initialize()
	 */
	@Override
	public void initialize(){
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		this.service = service;
		service.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					for(Iterator<Entry<K, V>> iter = runningSessions.entrySet().iterator(); iter.hasNext();){
						Entry<K, V> e = iter.next();
						if(e.getValue().getConnectionState().equals(DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN) || 
								e.getValue().getConnectionState().equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE)){
							if(LOGGER.isDebugEnabled()){
								LOGGER.debug("Session entry " + e.getKey().toString()+ " with state: " + e.getValue().getConnectionState().toString() + " will be removed.");
							}
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
		}, this.sessionCleanUpInterval, this.sessionCleanUpInterval, TimeUnit.MILLISECONDS);
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.SessionManager#terminate()
	 */
	@Override
	public void terminate(){
		this.service.shutdownNow();
		
		for (V session : this.runningSessions.values()) {
			session.terminate();
		}
		this.runningSessions.clear();
	}
	
}
