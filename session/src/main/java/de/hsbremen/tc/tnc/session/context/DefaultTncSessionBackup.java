package de.hsbremen.tc.tnc.session.context;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.client.TnccSessionLink;
import de.hsbremen.tc.tnc.clients.batch.TnccsBatch;
import de.hsbremen.tc.tnc.clients.handler.TnccsMessageHandler;
import de.hsbremen.tc.tnc.clients.message.TnccsMessage;
import de.hsbremen.tc.tnc.session.state.SessionState;
import de.hsbremen.tc.tnc.transport.connection.IfTConnection;

public class DefaultTncSessionBackup implements SessionContext{

	private TnccsMessageHandler messageHandler;
	
	private SessionState state;

	private final IfTConnection connection;
	
	private List<IMCConnection> imcConnections;
	
	private final TncContext tncc;
	
	private TnccsBatch currentBatch;
	
	private boolean shutdown;
	
	public DefaultTncSessionBackup(final TncContext client, final IfTConnection connection, final TnccsMessageHandler messageHandler){
		this.tncc = client;
		this.connection = connection;
		this.messageHandler = messageHandler;
		// this.state = new intitialState();
	}
	
	@Override
	public void initSession(){
		
	}
	
	@Override
	public void setState(SessionState state){
		this.state = state;
	}
	
	@Override
	public TnccsBatch getCurrentBatch() {
		if(currentBatch == null){
			this.receiveBatch();
		}
		return currentBatch;
	}

	@Override
	public void receiveBatch(){
		// receive and cache batch
		this.currentBatch = connection.receive();
	}	
	
	@Override
	public void sendBatch(TnccsBatch b){
		connection.send(b);
	}
	

	@Override
	public void handleBatch(TnccsBatch b) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleMessage(TnccsMessage m){
		messageHandler.handle(m);
	}
	

	@Override
	public void requestHandshake(){
		// check state
		// if possible set state containing begin handshake
	}
	
	@Override
	public void cancel(){
		connection.close();
	}

	/* (non-Javadoc)
	 * @see org.trustedcomputinggroup.tnc.other.TncSession#run()
	 */
	@Override
	public void run() {
		this.state.handle(this);
	}

	
	
}
