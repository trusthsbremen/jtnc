package de.hsbremen.tc.tnc.tnccs.client;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeTypeEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionFactory;
import de.hsbremen.tc.tnc.tnccs.session.base.Session;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public class DefaultClientFacade implements ClientFacade, GlobalHandshakeRetryListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientFacade.class);
	
	private static final long DEFAULT_SESSION_CLEANUP_INTERVAL = 5000; 
	
	private final Map<TransportConnection,Session> runningSessions;
	private final SessionFactory sessionFactory;
	private boolean started;
	private final long sessionCleanUpInterval;
	
	private ExecutorService sessionCleaner;
	
	public DefaultClientFacade(SessionFactory factory, long sessionCleanUpInterval){

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
	
		Session s = this.sessionFactory.createTnccsSession(connection);
		s.start(connection.isSelfInititated());
		
		this.runningSessions.put(connection, s);
		
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
	
	@Override
	public void start(){
		this.started = true;
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		this.sessionCleaner = service;
		service.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()){
					for(Iterator<Entry<TransportConnection, Session>> iter = runningSessions.entrySet().iterator(); iter.hasNext();){
						Entry<TransportConnection, Session> e = iter.next();
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
		}, this.sessionCleanUpInterval, this.sessionCleanUpInterval, TimeUnit.MILLISECONDS);

	}

	
	@Override
	public void stop() {
		this.sessionCleaner.shutdownNow();
		
		if(!started){
			throw new IllegalStateException("Client was not started.");
		}
		
		this.notifyGlobalConnectionChange(ConnectionChangeTypeEnum.CLOSED);
		
	}

	@Override
	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason)
			throws TncException {
		// TODO make this possible?
		throw new TncException("Global handshake retry is not supported.", TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
		
	}

}
