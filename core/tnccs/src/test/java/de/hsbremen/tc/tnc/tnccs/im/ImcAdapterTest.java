package de.hsbremen.tc.tnc.tnccs.im;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

public class ImcAdapterTest {

	private ImcAdapter adapter;
	private long imId0 = 1;
	private ImcConnectionAdapter connectionAdapter;

	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		ImcAdapterFactory factory = new ImcAdapterFactoryIetf();
		this.adapter = factory.createImcAdapter(Dummy.getIMC(),this.imId0);
		ImcConnectionAdapterFactory connfactory = new ImcConnectionAdapterFactoryIetf(Dummy.getConnectionContext());
		this.connectionAdapter = connfactory.createConnection(this.imId0);
	}
	
	@Test
	public void testBeginHandshakeTimeout() throws TncException, TerminatedException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test beginHandshake method."));
		Assert.assertEquals(imId0, this.adapter.getPrimaryId());
		this.adapter.beginHandshake(this.connectionAdapter);
		Assert.assertFalse(this.connectionAdapter.isReceiving());
	}
	
	@Test
	public void testBatchEnding() throws TncException, TerminatedException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test batchEnding method."));
		this.adapter.batchEnding(this.connectionAdapter);
		Assert.assertFalse(this.connectionAdapter.isReceiving());
	}
	
	@Test
	public void testSendMessage() throws TncException, ValidationException, TerminatedException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test sendMessage method."));
		this.adapter.handleMessage(this.connectionAdapter, Dummy.getPaAssessmentResult().getValue());
		Assert.assertFalse(this.connectionAdapter.isReceiving());
	}
	
	@Test
	public void testNotifyConnectionChange() throws TncException, TerminatedException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test notifyConnectionChange method."));
		this.adapter.notifyConnectionChange(this.connectionAdapter, DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		Assert.assertFalse(this.connectionAdapter.isReceiving());
	}
	
	@Test(expected=TerminatedException.class)
	public void testTerminate() throws TncException, TerminatedException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test terminate method."));
		this.adapter.terminate();
		Assert.assertFalse(this.connectionAdapter.isReceiving());
		try{
			this.adapter.batchEnding(this.connectionAdapter);
		}catch(TerminatedException e){
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
}
