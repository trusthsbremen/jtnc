package de.hsbremen.tc.tnc.tnccs.session.connection;

import java.io.ByteArrayOutputStream;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class TnccsConnectionTest {

	private TransportConnection connection;
	private TnccsInputChannelListener listener;
	private TnccsInputChannel in;
	private TnccsOutputChannel out;

	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}

	@Before
	public void setUp(){
		this.connection = Dummy.getSelfInitiatedTransportConnection();
		this.listener = Dummy.getInputChannelListener();
		
		TnccsChannelFactory channelFactory = Dummy.getChannelFactory();
		
		this.in = channelFactory.createInputChannel(connection, listener);
		this.out = channelFactory.createOutputChannel(connection);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDoubleRegister(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test repetitive listener registration."));
		this.in.register(listener);
		
	}
	
	@Test(expected=ConnectionException.class)
	public void testOpen() throws ComprehensibleException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test send on unopened connection."));
		try{
			this.out.send(Dummy.getBatchWithImMessage());
		}catch(SerializationException | ConnectionException e){
			System.out.println("Stream exception caught: " + e.getMessage());
			throw e;
		}catch(ValidationException e){
			System.out.println("Validation exception caught: " + e.getMessage() + " Something is wrong wih the test data.");
		}
	}
	
	@Test
	public void testSend() throws ComprehensibleException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test send batch."));
		try{
			this.connection.open();
			this.out.send(Dummy.getBatchWithImMessage());
		}catch(SerializationException | ConnectionException e){
			System.out.println("Stream exception caught: " + e.getMessage());
			throw e;
		}catch(ValidationException e){
			System.out.println("Validation exception caught: " + e.getMessage() + " Something is wrong wih the test data.");
		}
		
		byte[] b = ((ByteArrayOutputStream)connection.getOutputStream()).toByteArray();
		Assert.assertArrayEquals(Dummy.getBatchWithImMessageAsByte(), b);
		
	}
	
	@Test
	public void testReceive() throws ComprehensibleException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test receive batch with listener and close."));
		
		this.connection.open();
		Thread t = new Thread(this.in);
		t.start();
		Assert.assertEquals(true, this.connection.isOpen());
		
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PbBatch b = (PbBatch)((Dummy.TestTnccsInputChannelListener)this.listener).getBatch().getResult();
		
		Assert.assertEquals(PbBatchTypeEnum.CDATA, b.getHeader().getType());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		
		Assert.assertEquals(false, this.connection.isOpen());
		
	}
	
	@Test(expected=ConnectionException.class)
	public void asyncClose() throws ComprehensibleException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test asynchronous close on input, which should close output to."));
		this.connection.open();
		Thread t = new Thread(this.in);
		t.start();
		Assert.assertEquals(true, this.connection.isOpen());
		
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.out.send(Dummy.getBatchWithImMessage());
		} catch (ConnectionException | SerializationException | ValidationException e) {
			System.out.println(e.getMessage());
			throw e;
		}
		
	}
	
	
}
