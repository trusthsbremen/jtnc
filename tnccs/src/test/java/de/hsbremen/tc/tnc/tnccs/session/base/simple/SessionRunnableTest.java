package de.hsbremen.tc.tnc.tnccs.session.base.simple;

import java.util.Arrays;
import java.util.concurrent.Executors;

import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public class SessionRunnableTest {

	private DefaultSessionRunnable session;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		this.session = new DefaultSessionRunnable(
				new DefaultSessionAttributes(TcgTnccsProtocolEnum.TNCCS.toString(), TcgTnccsVersionEnum.V2.value()),
				PbWriterFactory.createProductionDefault(),
				PbReaderFactory.createProductionDefault(),
				Executors.newSingleThreadExecutor());
		this.session.registerConnection(Dummy.getTransportConnection());
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
		this.waitShortly(10);
		Assert.assertFalse(this.session.isClosed());
		
		Thread[] runningThreads = new Thread[5];
		Thread.enumerate(runningThreads);
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
			}
		}
		this.session.close();
		this.waitShortly(100);
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
				System.out.println(i + "--" + runningThreads[i].getName() + " : alive - " + runningThreads[i].isAlive());
			}
		}
		
		Assert.assertTrue(this.session.isClosed());
	}
	
	@Test
	public void testReceiveMessage(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test receive message."));
		this.session.registerStatemachine(Dummy.getStateMachine());
		Assert.assertTrue(this.session.isClosed());
		this.session.start(true);
		this.waitShortly(10);
		Assert.assertFalse(this.session.isClosed());
		
		Thread[] runningThreads = new Thread[5];
		Thread.enumerate(runningThreads);
		System.out.println(Arrays.toString(runningThreads));
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
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
		this.waitShortly(10);
		Assert.assertFalse(this.session.isClosed());
		this.session.handle(new SerializationException("Something happend", true));
		this.waitShortly(10);
		Assert.assertTrue(this.session.isClosed());
		Thread[] runningThreads = new Thread[5];
		Thread.enumerate(runningThreads);
		System.out.println(Arrays.toString(runningThreads));
		for(int i = 0; i < runningThreads.length; i++){
			
			if(runningThreads[i] != null){
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
		this.waitShortly(10);
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
		this.waitShortly(10);
		Assert.assertFalse(this.session.isClosed());
		this.session.close();
		this.waitShortly(10);
		this.session.retryHandshake(ImHandshakeRetryReasonEnum.TNC_RETRY_REASON_IMC_SERIOUS_EVENT);

	}
	
	private void waitShortly(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
