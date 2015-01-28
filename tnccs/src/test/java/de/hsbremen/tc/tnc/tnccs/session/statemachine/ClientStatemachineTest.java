package de.hsbremen.tc.tnc.tnccs.session.statemachine;


import java.util.List;

import org.ietf.nea.pb.batch.DefaultTnccsBatchContainer;
import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolEnum;
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsVersionEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultSessionAttributes;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultClientStateFactory;

public class ClientStatemachineTest {

	private StateMachine machine;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		DefaultSessionAttributes attributes = new DefaultSessionAttributes(TcgTnccsProtocolEnum.TNCCS.value(), TcgTnccsVersionEnum.V2.value());
		TnccContentHandler handler = new DefaultTnccContentHandler(Dummy.getImcHandler(), 
				new DefaultTnccHandler(attributes), 
				new DefaultTnccsValidationExceptionHandler(new AttributeCollection()));
		this.machine = new DefaultClientStateMachine(new DefaultClientStateFactory(attributes, handler));
	}
	
	@Test
	public void testStartSelfInitiated(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test self inititated start."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);
			
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			boolean hasIm = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
				if(message.getValue() instanceof PbMessageValueIm){
					hasIm = true;
				}
			}
			
			Assert.assertTrue(hasLanguage);
			Assert.assertTrue(hasIm);
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected=StateMachineAccessException.class)
	public void testStartNoSelfRepeatedly() throws StateMachineAccessException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test faulty repeated start."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(false);
			
			if(batch != null){
				Assert.fail();
			}
			
			batch = (PbBatch)this.machine.start(false);
			
		} catch (StateMachineAccessException e) {
			System.out.println("Exception caught:" + e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void testStartNoSelf(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test start not self initiated."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(false);

			if(batch != null){
				Assert.fail();
			}
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testClose(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test close."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(false);

			if(batch != null){
				Assert.fail();
			}
			
			this.machine.stop();
			
			Assert.assertTrue(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCloseRestart(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test close and restart."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(false);

			if(batch != null){
				Assert.fail();
			}
			
			this.machine.stop();
			
			Assert.assertTrue(this.machine.isClosed());
			
			batch = (PbBatch)this.machine.start(false);
			
			Assert.assertFalse(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTransitionsNoSelf(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test transitions, if started as not self initiated."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(false);

			if(batch != null){
				Assert.fail();
			}
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getServerDataBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);

			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			
			Assert.assertFalse(this.machine.canRetry());
			
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
			}
			Assert.assertTrue(hasLanguage);
			
			container = new DefaultTnccsBatchContainer(Dummy.getServerResultBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.canRetry());
			
			container = new DefaultTnccsBatchContainer(Dummy.getServerCloseBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTransitionsSelfInitiated(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test transitions, if self initiated."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);

			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
			}
			Assert.assertTrue(hasLanguage);
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getServerResultBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.canRetry());
			
			container = new DefaultTnccsBatchContainer(Dummy.getServerCloseBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStartSelfInitiatedAndEarlyClose(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test self initiated start and early close."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);

			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
			}
			Assert.assertTrue(hasLanguage);
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getServerCloseBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.isClosed());
			
		
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStartSelfInitiatedAndUnexpectedBatch(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test self initiated start and unexpected batch."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);

			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
			}
			Assert.assertTrue(hasLanguage);
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getClientDataBatchWithImMessage(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CLOSE);
			
			Assert.assertTrue(this.machine.isClosed());
			
		
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected=TncException.class)
	public void testHandshakeRetryNotAllowed() throws TncException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handshake retry not allowed."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);

			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.CDATA);
			
			List<PbMessage> messages = batch.getMessages();
			if(messages == null || messages.isEmpty()){
				Assert.fail();
			}
			
			boolean hasLanguage = false;
			for (PbMessage message : messages) {
				if(message.getValue() instanceof PbMessageValueLanguagePreference){
					hasLanguage = true;
				}
			}
			Assert.assertTrue(hasLanguage);
			
			try {
				this.machine.retryHandshake(ImHandshakeRetryReasonEnum.TNC_RETRY_REASON_IMC_PERIODIC);
			} catch (TncException e) {
				System.out.println("Exception caught: " + e.getMessage());
				throw e;
			}
			
			
			Assert.assertFalse(this.machine.isClosed());
			
		
		} catch (StateMachineAccessException e) {
			e.printStackTrace();
		}
	}
	
}
