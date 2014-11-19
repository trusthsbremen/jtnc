package de.hsbremen.tc.tnc.session.context;

import de.hsbremen.tc.tnc.exception.HandlingException;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.session.TncContext;
import de.hsbremen.tc.tnc.session.context.enums.SessionEventEnum;
import de.hsbremen.tc.tnc.session.state.SessionState;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class Session implements SessionContext, TncSession{

	private TnccsBatch currentBatch;
	private SessionState state;
	private boolean shutdown;
	private TncContext context;
	private IfTConnection connection;
	
	
	public Session(TncContext context, SessionState startState, IfTConnection connection){
		this.shutdown = false;
		this.currentBatch = null;
		
		this.context = context;
		this.connection = connection;
		// usally the init state if connection is self initiated or the corresponding server/client working state if
		// session is not self initiated
		this.state = startState;
	}
	
	@Override
	public void run() {
		while(!shutdown){
			this.state = state.handle(this);
		}
		this.connection.close();
		this.context.notifyClient(connection.getId(), SessionEventEnum.SHUTDOWN, null);
	}

	
	
	/* Session Context */

	@Override
	public boolean isServerSession() {
		return context.isServer();
	}

	@Override
	public TnccsBatch getCurrentBatch() {
		return this.currentBatch;
	}

	@Override
	public TnccsBatch receiveBatch() throws SerializationException, ConnectionException, ValidationException {
		this.currentBatch = null;
		this.currentBatch = this.connection.receive();
		return this.currentBatch;
	}

	@Override
	public void sendBatch(TnccsBatch b) throws SerializationException, ConnectionException {
		this.currentBatch = b;
		if(b != null){
			this.connection.send(b);
		}
	}

	@Override
	public void handleBatch(TnccsBatch b) throws HandlingException{
		if(b != null){
			// TODO Auto-generated method stub
		}
	}
	
	@Override
	public void closeSession(){
		// TODO notify close to IMC/IMV
		this.shutdown = true;
	}
	
	/* TNCC/S Link */
	
	@Override
	public void requestHandshake() {
		// TODO Auto-generated method stub
		
	}

}
