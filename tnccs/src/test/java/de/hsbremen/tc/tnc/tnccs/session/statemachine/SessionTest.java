package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.SessionAttributes;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsChannelFactory;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.session.Session;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class SessionTest {

	private Session session;
	private TnccsChannelFactory channelFactory;
	private TransportConnection connection;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		this.connection = Dummy.getSelfInitiatedTransportConnection();
		this.channelFactory = Dummy.getChannelFactory();
		this.session = new Session(new SessionAttributes(TcgTnccsProtocolEnum.TNCCS.toString(), TcgTnccsVersionEnum.V2.value()));
		this.session.registerInput(this.channelFactory.createInputChannel(this.connection,this.session));
		this.session.registerOutput(this.channelFactory.createOutputChannel(this.connection));
		try {
			this.connection.open();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testStartButStatemachineMissing(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test early start."));
		this.session.start(true);
	}
	
	@Test
	public void testStartAndClose(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test start and close."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		Assert.assertFalse(this.session.isClosed());
		this.session.close();
		Assert.assertTrue(this.session.isClosed());
	}
	
	@Test
	public void testReceiveMessage(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test receive message."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		Assert.assertFalse(this.session.isClosed());
		
		Thread[] runningThreads = new Thread[5];
		Thread.enumerate(runningThreads);
		System.out.println(Arrays.toString(runningThreads));
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
				if(runningThreads[i].getName().contains("InputChannelThread")){
					try {
						runningThreads[i].join();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
			}
		}
	}
	
	@Test
	public void testHandleError(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle error."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		Assert.assertFalse(this.session.isClosed());
		this.session.handle(new SerializationException("Something happend", true));
		Assert.assertTrue(this.session.isClosed());
		Thread[] runningThreads = new Thread[5];
		Thread.enumerate(runningThreads);
		System.out.println(Arrays.toString(runningThreads));
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
				if(runningThreads[i].getName().contains("InputChannelThread")){
					try {
						runningThreads[i].join();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
			}
		}
	}
	
	@Test
	public void testRetryNotification(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request handshake retry."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		Assert.assertFalse(this.session.isClosed());
		try {
			this.session.retryHandshake(ImHandshakeRetryReasonEnum.TNC_RETRY_REASON_IMC_SERIOUS_EVENT);
		} catch (TncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected=TncException.class)
	public void testRetryNotificationOnClose() throws TncException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request handshake retry after close."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		Assert.assertFalse(this.session.isClosed());
		this.session.close();
		this.session.retryHandshake(ImHandshakeRetryReasonEnum.TNC_RETRY_REASON_IMC_SERIOUS_EVENT);

	}
}
