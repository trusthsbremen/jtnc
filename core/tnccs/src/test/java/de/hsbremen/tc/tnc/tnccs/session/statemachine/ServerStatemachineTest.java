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
import de.hsbremen.tc.tnc.message.tnccs.enums.TcgTnccsProtocolBindingEnum;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.HandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.simple.DefaultTncsHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.simple.DefaultSessionAttributes;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.simple.DefaultServerStateMachine;

public class ServerStatemachineTest {

	private StateMachine machine;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		DefaultSessionAttributes attributes = new DefaultSessionAttributes(TcgTnccsProtocolBindingEnum.TNCCS2);
		TncsContentHandler handler = new DefaultTncsContentHandler(Dummy.getImvHandler(), 
				new DefaultTncsHandler(attributes), 
				new DefaultTnccsValidationExceptionHandler(new AttributeCollection()));
		this.machine = new DefaultServerStateMachine(new DefaultServerStateHelper(attributes,handler));
	}
	
	@Test
	public void testStartSelfInitiated(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test self inititated start."));
		try {
			PbBatch batch = (PbBatch)this.machine.start(true);
			
			Assert.assertEquals(batch.getHeader().getType(), PbBatchTypeEnum.SDATA);
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
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
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getClientDataBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);

			Assert.assertFalse(this.machine.isClosed());
			Assert.assertEquals(PbBatchTypeEnum.SDATA,batch.getHeader().getType());
			
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
			
			container = new DefaultTnccsBatchContainer(Dummy.getEmptyClientDataBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);
			
			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertEquals(PbBatchTypeEnum.RESULT, batch.getHeader().getType());
			
			Assert.assertTrue(this.machine.canRetry());
			
			container = new DefaultTnccsBatchContainer(Dummy.getClientCloseBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			System.err.println(e.getMessage());
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
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
			Assert.assertEquals(PbBatchTypeEnum.SDATA, batch.getHeader().getType());
			
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
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getEmptyClientDataBatch(), null);
			
			batch = (PbBatch)this.machine.receiveBatch(container);
			
			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertEquals(PbBatchTypeEnum.RESULT, batch.getHeader().getType());
			
			Assert.assertTrue(this.machine.canRetry());
			
			container = new DefaultTnccsBatchContainer(Dummy.getClientCloseBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch != null){
				Assert.fail();
			}
			
			Assert.assertTrue(this.machine.isClosed());
			
		} catch (StateMachineAccessException e) {
			System.err.println(e.getMessage());
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
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
			Assert.assertEquals(PbBatchTypeEnum.SDATA,batch.getHeader().getType());
			
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
			System.err.println(e.getMessage());
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
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
			Assert.assertEquals(PbBatchTypeEnum.SDATA, batch.getHeader().getType());
			
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
			
			TnccsBatchContainer container = new DefaultTnccsBatchContainer(Dummy.getServerDataBatch(), null);
			
			batch = (PbBatch) this.machine.receiveBatch(container);
			
			if(batch == null){
				Assert.fail();
			}
			
			Assert.assertEquals(PbBatchTypeEnum.CLOSE, batch.getHeader().getType());
			
			Assert.assertTrue(this.machine.isClosed());
			
		
		} catch (StateMachineAccessException e) {
			System.err.println(e.getMessage());
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
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
			Assert.assertEquals( PbBatchTypeEnum.SDATA, batch.getHeader().getType());
			
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
				this.machine.retryHandshake(HandshakeRetryReasonEnum.TNC_RETRY_REASON_IMV_PERIODIC);
			} catch (TncException e) {
				System.out.println("Exception caught: " + e.getMessage());
				throw e;
			}
			
			
			Assert.assertFalse(this.machine.isClosed());
			
		
		} catch (StateMachineAccessException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
