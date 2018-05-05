package de.hsbremen.tc.tnc.im;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnection;

import de.hsbremen.tc.tnc.im.example.TestImc;
import de.hsbremen.tc.tnc.im.example.TestImv;
import de.hsbremen.tc.tnc.im.example.adapter.TestImcConnection;
import de.hsbremen.tc.tnc.im.example.adapter.TestImvConnection;

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
		this.imc = new TestImc();
		this.imv = new TestImv();
		this.imcConnection = new TestImcConnection(this.imv);
		this.imvConnection = new TestImvConnection(this.imc);
		((TestImcConnection)this.imcConnection).setImvConnection(this.imvConnection);
		((TestImvConnection)this.imvConnection).setImcConnection(this.imcConnection);
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
	
}
