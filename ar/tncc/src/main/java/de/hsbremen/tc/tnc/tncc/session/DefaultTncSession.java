package de.hsbremen.tc.tnc.tncc.session;

import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

import de.hsbremen.tc.tnc.nar.connection.IfTConnection;
import de.hsbremen.tc.tnc.tncc.client.InternalTnccInterface;
import de.hsbremen.tc.tnc.tncc.sessionstate.SessionState;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.handler.TnccsMessageHandler;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class DefaultTncSession implements TncSession{

	private TnccsMessageHandler messageHandler;
	
	private SessionState state;

	private final IfTConnection connection;
	
	private List<IMCConnection> imcConnections;
	
	private final InternalTnccInterface tncc;
	
	private TnccsBatch currentBatch;
	
	public DefaultTncSession(final InternalTnccInterface client, final IfTConnection connection, TnccsMessageHandler messageHandler){
		this.tncc = client;
		this.connection = connection;
		this.messageHandler = messageHandler;
		// this.state = new intitialState();
	}
	
	void intializeSession(){
		
	}
	
	void setState(SessionState state){
		this.state = state;
	}
	
	// TODO throws exception if not working
	TnccsBatch receiveBatch(){
		// receive and cache batch
		return currentBatch != null ? currentBatch : connection.receive();
	}	
	
	// TODO throws excpetions if not working
	void sendBatch(TnccsBatch b){
		// delete old received batch and send the new batch
		currentBatch = null;
		connection.send(b);
	}
	
	void handleMessage(TnccsMessage m){
		messageHandler.handle(m);
	}
	

	@Override
	public void requestHandshake(){
		// check state
		// if possible set state containing begin handshake
	}
	
	@Override
	public void close(){
		
	}

	/* (non-Javadoc)
	 * @see org.trustedcomputinggroup.tnc.other.TncSession#run()
	 */
	@Override
	public void run() {
		this.state.handle(this);
		
	}
	
}
