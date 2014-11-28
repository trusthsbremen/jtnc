package de.hsbremen.tc.tnc.im;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.data.ImRawComponent;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetfLong;

public class ImcTest {

	IMC imc;
	IMC imcLong;
	
	@BeforeClass
	public static void logSetUp(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		this.imc = new ImcAdapterIetf();
		this.imcLong = new ImcAdapterIetfLong();
		
	}
	
	@Test
	public void testInitialize() throws TNCException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test initialize method."));
		this.initializeImc();
	}
	
	@Test(expected = TNCException.class)
	public void testDoubleInitialize() throws TNCException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test error for redundant initialization."));
		try{
			this.initializeImc();
			this.initializeImc();
		}catch(TNCException e){
			if(e.getResultCode() == TNCException.TNC_RESULT_ALREADY_INITIALIZED){
				throw e;
			}else{
				System.err.println("Unexpected error was thrown. " +  e.getMessage());
			}
		}
	}
	
	@Test(expected = TNCException.class)
	public void testNotInitialized() throws TNCException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(), "Test error for missing initialization."));
		try{
			this.imc.notifyConnectionChange(Dummy.getIMCConnection(),DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_CREATE.state());
		}catch(TNCException e){
			if(e.getResultCode() == TNCException.TNC_RESULT_NOT_INITIALIZED){
				throw e;
			}else{
				System.err.println("Unexpected error was thrown. " +  e.getMessage());
			}
		}
	}
	
	@Test
	public void testNotifyConnectionChange() throws TNCException{
		
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(), "Test notify connection change with CREATE."));
		try {
			this.initializeImc();
		} catch (TNCException e) {
			e.printStackTrace();
		}
		
		this.imc.notifyConnectionChange(Dummy.getIMCConnection(), DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_CREATE.state());
	}
	
	@Test
	public void testBeginHandshake() throws TNCException{

		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(), "Test begin handshake."));
		try {
			this.initializeImc();
		} catch (TNCException e) {
			e.printStackTrace();
		}
		this.imc.beginHandshake(Dummy.getIMCConnection());
		
	}
	
	@Test
	public void testBatchEnding() throws TNCException{

		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(), "Test batch ending."));
		try {
			this.initializeImc();
		} catch (TNCException e) {
			e.printStackTrace();
		}
		this.imc.batchEnding(Dummy.getIMCConnection());
		
	}
	
	@Test
	public void testReceiveMessage() throws TNCException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(), "Test receive message."));
		ImRawComponent component = Dummy.getRawComponentForImc();
		long messageType = (component.getVendorId() << 8) | (component.getType() & 0xFF);
		
		try {
			this.initializeImc();
		} catch (TNCException e) {
			e.printStackTrace();
		}

		this.imc.receiveMessage(Dummy.getIMCConnection(),messageType, component.getMessage());
	}
		
	
	private void initializeImc() throws TNCException{
		this.imc.initialize(Dummy.getTncc());
//		if(this.imc instanceof AttributeSupport){
//			((AttributeSupport) imc).setAttribute(AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID, new Long(new Random().nextInt(100)));
//		}
	}
	
	
}
