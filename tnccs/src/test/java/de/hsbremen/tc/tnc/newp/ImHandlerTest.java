package de.hsbremen.tc.tnc.newp;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.adapter.connection.DefaultImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.newp.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouter;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

public class ImHandlerTest {

	private TnccsMessageHandler handler;
	private ImManager<IMC> manager;
	private ImMessageRouter router;
	private ImcAdapterFactory imcFactory;
	private TnccAdapterFactory tnccFactory;
	private ImcConnectionAdapterFactory connFactory;
	private ImcHandlerFactory handlerFactory;
	private SupportedMessageType type;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp() throws ImInitializeException{
		
		this.imcFactory = new ImcAdapterFactoryIetf();
		this.tnccFactory = new TnccAdapterFactoryIetf(Dummy.getRetryListener());
		this.router = new DefaultImMessageRouter();
		this.manager = new DefaultImcManager(this.router,this.imcFactory,this.tnccFactory);
		Set<SupportedMessageType> types = new HashSet<>();
		this.type = SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0001);
		types.add(type);
		types.add(SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0003));
		this.manager.add(Dummy.getIMCwithMessageSupport(types));
		
		this.connFactory = new DefaultImcConnectionAdapterFactory(Dummy.getConnectionContext());
		this.handlerFactory = new DefaultImcHandlerFactory((ImAdapterManager<ImcAdapter>)this.manager);
		this.handler = this.handlerFactory.getHandler(this.connFactory);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConnectionStateUnknown(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test connection state unknown."));
		this.handler.requestMessages(Dummy.getSessionContext());
	}
	
	@Test
	public void testSetConnectionStateAndRequestMessage(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request message and connection state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		this.handler.requestMessages(Dummy.getSessionContext());
		this.handler.requestMessages(Dummy.getSessionContext());
	}
	
	@Test
	public void testSetConnectionStateAndRequestMessage2(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request message and connection state, unimportant state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE);
		this.handler.requestMessages(Dummy.getSessionContext());
	}
	
	@Test
	public void testSetConnectionStateAndForwardMessage() throws ValidationException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test forward message and connection state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		this.handler.forwardMessage(Dummy.getSessionContext(), Dummy.getPaAssessmentResult());
		this.handler.requestMessages(Dummy.getSessionContext());
	}
	
}
