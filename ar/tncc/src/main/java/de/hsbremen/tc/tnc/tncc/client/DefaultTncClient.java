package de.hsbremen.tc.tnc.tncc.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hsbremen.tc.tnc.nar.connection.IfTConnection;
import de.hsbremen.tc.tnc.tncc.session.TncSession;

public class DefaultTncClient implements InternalTnccInterface, ExternalInterface {

	
	private long imcCounter;
    //private List<ImcContainer> imcList;
    private  ExecutorService executor;
    private Map<IfTConnection,TncSession> activeSessions;

    public DefaultTncClient(){
    	// Thread calculation copied from Jetty SelectChannelConnector
    	this.executor = Executors.newFixedThreadPool(Math.max(1, (Runtime.getRuntime().availableProcessors() + 3)/4));
    	this.activeSessions = new HashMap<>();
    }
    
    
    
    private void createSession(){
    	//TODO
    }
	
	@Override
	public void sessionClosed(TncSession session) {
		// TODO Auto-generated method stub

	}


	@Override
	public void requestHandshake(IfTConnection connection) {
		if(connection != null && connection.isOpen()){
			if(this.activeSessions.containsKey(connection)){
				this.activeSessions.get(connection).requestHandshake();
			}else{
				this.createSession();
			}
		}
	}
}
