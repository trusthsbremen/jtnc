package de.hsbremen.tc.tnc.tnccs.im;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.DefaultImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tncc.TnccAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.tncc.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.im.handler.DefaultImcHandler;
import de.hsbremen.tc.tnc.tnccs.im.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.im.manager.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImcManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public class ImHandlerTest {

	private ImcHandler handler;
	private ImcManager manager;
	private ImMessageRouter router;
	private ImcAdapterFactory imcFactory;
	private TnccAdapterFactory tnccFactory;
	private ImcConnectionAdapterFactory connFactory;
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
		ImcConnectionContext context = Dummy.getConnectionContext();
		this.connFactory = new DefaultImcConnectionAdapterFactory(context);
		this.handler = new DefaultImcHandler(manager, connFactory, context, manager.getRouter());

	}
	
	@Test(expected=IllegalStateException.class)
	public void testConnectionStateUnknown(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test connection state unknown."));
		this.handler.requestMessages();
	}
	
	@Test
	public void testSetConnectionStateAndRequestMessage(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request message and connection state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		this.handler.requestMessages();
		this.handler.requestMessages();
	}
	
	@Test
	public void testSetConnectionStateAndRequestMessage2(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test request message and connection state, unimportant state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE);
		this.handler.requestMessages();
	}
	
	@Test
	public void testSetConnectionStateAndForwardMessage() throws ValidationException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test forward message and connection state."));
		this.handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		this.handler.forwardMessage(Dummy.getPaAssessmentResult());
		this.handler.requestMessages();
	}
	
}
