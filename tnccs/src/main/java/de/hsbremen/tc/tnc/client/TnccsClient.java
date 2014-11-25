package de.hsbremen.tc.tnc.client;

import java.util.HashMap;
import java.util.Map;

import de.hsbremen.tc.tnc.client.enums.TnccsConnectionChangeEventEnum;
import de.hsbremen.tc.tnc.client.exception.NoImIdsLeftException;
import de.hsbremen.tc.tnc.session.TnccsSessionFactory;
import de.hsbremen.tc.tnc.session.context.TncSession;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public class TnccsClient implements TnccsConnector{

	public static final boolean SERVER = Boolean.TRUE;
	public static final boolean CLIENT = Boolean.FALSE;

	private final TnccsSessionFactory sessionFactory;
	private final Map<TransportConnection,TncSession> activeSessions;
	private boolean role;
	
	public TnccsClient(boolean role, TnccsSessionFactory sessionFactory){
		this.role = role;
		this.sessionFactory = sessionFactory;
		this.activeSessions = new HashMap<>();
	}
	
	@Override
	public void initialize() throws NoImIdsLeftException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyConnectionChange(TnccsConnectionChangeEventEnum event,
			TransportConnection connection){
	
		TncSession session = this.findSessionByConnection(connection);
		if(event.id() == TnccsConnectionChangeEventEnum.HANDSHAKE.id()){
			session.requestHandshake();
		}else if(event.id() == TnccsConnectionChangeEventEnum.DELETE.id()){
			session.closeSession();
			this.activeSessions.remove(connection);
		}
		
	}
	
	private TncSession findSessionByConnection(TransportConnection connection){
		if(this.activeSessions.containsKey(connection)){
			return this.activeSessions.get(connection);
		}

		TncSession newSession = sessionFactory.createSession(connection);
		this.activeSessions.put(connection, newSession);
		
		return newSession;
		
	}

}
