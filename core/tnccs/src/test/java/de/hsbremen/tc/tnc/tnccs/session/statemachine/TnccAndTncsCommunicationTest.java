package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.ietf.nea.pb.batch.DefaultTnccsBatchContainer;
import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultSessionAttributes;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateMachine;

public class TnccAndTncsCommunicationTest {

	private StateMachine server;
	private StateMachine client;
	private BlockingDeque<TnccsBatch> clientBatchQueue;
	private BlockingDeque<TnccsBatch> serverBatchQueue;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
		
	}
	
	@Before
	public void setUp(){
		this.clientBatchQueue = new LinkedBlockingDeque<>();
		this.serverBatchQueue = new LinkedBlockingDeque<>();
		
		DefaultSessionAttributes serverAttributes = new DefaultSessionAttributes(TcgTnccsProtocolBindingEnum.TNCCS2);
		TncsContentHandler tncsHandler = new DefaultTncsContentHandler(Dummy.getImvHandler(), 
				new DefaultTncsHandler(serverAttributes), 
				new DefaultTnccsValidationExceptionHandler(new AttributeCollection()));
		this.server = new DefaultServerStateMachine(new DefaultServerStateHelper(serverAttributes,tncsHandler));
		
		DefaultSessionAttributes clientAttributes = new DefaultSessionAttributes(TcgTnccsProtocolBindingEnum.TNCCS2);
		TnccContentHandler tnccHandler = new DefaultTnccContentHandler(Dummy.getImcHandler(), 
				new DefaultTnccHandler(clientAttributes), 
				new DefaultTnccsValidationExceptionHandler(new AttributeCollection()));
		this.client = new DefaultClientStateMachine(new DefaultClientStateHelper(clientAttributes,tnccHandler));
	
	}
	
	@Test
	public void testClientInitiated(){
		final Thread t1 = new Thread(new Runnable() {
	
			@Override
			public void run() {
				try{
					clientBatchQueue.push(client.start(true));
					while(!client.isClosed()){
						TnccsBatch next = serverBatchQueue.takeFirst(); 
						boolean result =((PbBatch)next).getHeader().getType().equals(PbBatchTypeEnum.RESULT);
						TnccsBatch b = client.receiveBatch(new DefaultTnccsBatchContainer(next, null));
						if(b != null){
							clientBatchQueue.push(b);
						}
						
						if(result){
							b = client.close();
							if(b != null){
								clientBatchQueue.push(b);
							}
						}
						
					}
				}catch(StateMachineAccessException e){
					System.err.println("Statemachine access exception. " + e.getMessage() );
				}catch (InterruptedException e){
					System.err.println(e.getMessage());
				}
			
			}
		});
		
		
		final Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try{
					server.start(false);
					while(!server.isClosed()){
						TnccsBatch b = server.receiveBatch(new DefaultTnccsBatchContainer(clientBatchQueue.takeFirst(), null));
						if(b != null){
							serverBatchQueue.push(b);
						}
					}
				}catch(StateMachineAccessException e){
					System.err.println("Statemachine access exception. " + e.getMessage() );
				}catch (InterruptedException e){
					System.err.println(e.getMessage());
				}
			
			}
		});
		
		t1.start();
		t2.start();
		
		
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
