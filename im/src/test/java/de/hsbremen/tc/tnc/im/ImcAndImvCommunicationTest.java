package de.hsbremen.tc.tnc.im;

import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.imc.ImcAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.imv.ImvAdapterIetf;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.adapter.tncs.TncsAdapterFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluatorFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImcSessionFactory;
import de.hsbremen.tc.tnc.im.session.DefaultImvSessionFactory;
import de.hsbremen.tc.tnc.im.session.ImcSession;
import de.hsbremen.tc.tnc.im.session.ImvSession;
import de.hsbremen.tc.tnc.im.session.DefaultImSessionManager;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

public class ImcAndImvCommunicationTest {

	private IMC imc;
	private IMV imv;
	private IMCConnection imcConnection;
	private IMVConnection imvConnection;

	@BeforeClass
	public static void logSetUp(){
		Dummy.setLogSettings();
	}
	
	
	@Before
	public void setUp() throws org.trustedcomputinggroup.tnc.ifimc.TNCException, org.trustedcomputinggroup.tnc.ifimv.TNCException{
		this.imc = new TestImcOs();
		this.imv = new TestImvOs();
		this.imcConnection = new ImcConnection(this.imv);
		this.imvConnection = new ImvConnection(this.imc);
		((ImcConnection)this.imcConnection).setImvConnection(this.imvConnection);
		((ImvConnection)this.imvConnection).setImcConnection(this.imcConnection);
		this.imc.initialize(Dummy.getTncc());
		this.imv.initialize(Dummy.getTncs());
		
	}
	
	@Test
	public void testClientInitiatedAssessment() throws TNCException, org.trustedcomputinggroup.tnc.ifimv.TNCException{
		this.imc.notifyConnectionChange(this.imcConnection, TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE);
		this.imc.beginHandshake(this.imcConnection);
		this.imc.notifyConnectionChange(this.imcConnection, TNCConstants.TNC_CONNECTION_STATE_DELETE);
		this.imv.notifyConnectionChange(this.imvConnection, TNCConstants.TNC_CONNECTION_STATE_DELETE);
	}
	
	
	private class TestImcOs extends ImcAdapterIetf{
		
		public TestImcOs(){
			super(new ImParameter(), new TnccAdapterFactoryIetf(),
					new DefaultImcSessionFactory(),
					new DefaultImSessionManager<IMCConnection, ImcSession>(3000),
					new OsImcEvaluatorFactory(),
					new ImcConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
					PaReaderFactory.createProductionDefault());
		}
		
	}
	
	private class TestImvOs extends ImvAdapterIetf{
		
		public TestImvOs(){
			super(new ImParameter(), new TncsAdapterFactoryIetf(),
					new DefaultImvSessionFactory(),
					new DefaultImSessionManager<IMVConnection, ImvSession>(3000),
					new OsImvEvaluatorFactory("/os_imv.properties"),
					new ImvConnectionAdapterFactoryIetf(PaWriterFactory.createProductionDefault()),
					PaReaderFactory.createProductionDefault());
		}
		
	}
	
	private class ImvConnection implements IMVConnection{

		private IMCConnection imcc;
		private final IMC imc;
		
		
		
		public ImvConnection(IMC imc) {
			this.imc = imc;
		}

		public void setImcConnection(IMCConnection con){
			this.imcc = con;
		}
		
		@Override
		public void sendMessage(long messageType, byte[] message)
				throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
			System.err.println("Send Message received from IMV.");
			try {
				this.imc.receiveMessage(this.imcc, messageType, message);
				this.imc.notifyConnectionChange(this.imcc, TNCConstants.TNC_CONNECTION_STATE_ACCESS_ALLOWED);
			} catch (TNCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void requestHandshakeRetry(long reason)
				throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
			System.err.println("Handshake request received from IMV.");
			
		}

		@Override
		public void provideRecommendation(long recommendation, long evaluation)
				throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
			System.err.println("Recommendation provided at IMVConnection:\n" + ImvActionRecommendationEnum.fromId(recommendation) +", "+  ImvEvaluationResultEnum.fromId(evaluation) );
		}

		@Override
		public Object getAttribute(long attributeID)
				throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
			System.err.println("GetAttribute called with ID: " + attributeID );
			return null;
		}

		@Override
		public void setAttribute(long attributeID, Object attributeValue)
				throws org.trustedcomputinggroup.tnc.ifimv.TNCException {
			System.err.println("SetAttribute called with ID: " + attributeID );
			
		}
		
	}
	
	private class ImcConnection implements IMCConnection, AttributeSupport{

		private final IMV imv;
		private IMVConnection imvc;
		
		public ImcConnection(IMV imv) {
			this.imv = imv;
		}

		public void setImvConnection(IMVConnection con){
			this.imvc = con;
		}
		
		@Override
		public void sendMessage(long messageType, byte[] message)
				throws TNCException {
			System.err.println("Send Message received from IMC.");
			try {
				this.imv.notifyConnectionChange(this.imvc, TNCConstants.TNC_CONNECTION_STATE_HANDSHAKE);
				this.imv.receiveMessage(this.imvc, messageType, message);
				this.imv.notifyConnectionChange(this.imvc, TNCConstants.TNC_CONNECTION_STATE_ACCESS_ALLOWED);
			} catch (org.trustedcomputinggroup.tnc.ifimv.TNCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void requestHandshakeRetry(long reason) throws TNCException {
			System.err.println("Handshake request received from IMC.");
		}

		@Override
		public Object getAttribute(long attributeID)
				throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
			System.err.println("GetAttribute called with ID: " + attributeID );
			return null;
		}

		@Override
		public void setAttribute(long attributeID, Object attributeValue)
				throws org.trustedcomputinggroup.tnc.ifimc.TNCException {
			System.err.println("SetAttribute called with ID: " + attributeID );
			
		}
		
	}
	
}
