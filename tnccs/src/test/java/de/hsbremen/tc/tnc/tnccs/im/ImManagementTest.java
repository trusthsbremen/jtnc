package de.hsbremen.tc.tnc.tnccs.im;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.tnccs.TnccAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;
import de.hsbremen.tc.tnc.tnccs.im.manager.simple.DefaultImcManager;
import de.hsbremen.tc.tnc.tnccs.im.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

public class ImManagementTest {

	private ImManager<IMC> manager;
	private ImMessageRouter router;
	private ImcAdapterFactory imcFactory;
	private TnccAdapterFactory tnccFactory;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		this.imcFactory = new ImcAdapterFactoryIetf();
		this.tnccFactory = new TnccAdapterFactoryIetf(Dummy.getRetryListener());
		this.router = new DefaultImMessageRouter();
		this.manager = new DefaultImcManager(router,imcFactory,tnccFactory);
	}
	
	@Test
	public void testAddAndRemove() throws ImInitializeException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test add and remove IMC/V."));
		long id0 = this.manager.add(Dummy.getIMC());
		Assert.assertEquals(1, id0);
		long id1 = this.manager.add(Dummy.getIMC());
		Assert.assertEquals(2, id1);
	}
	
	@Test
	public void testIdReuse() throws ImInitializeException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test id reuse."));
		long id0 = this.manager.add(Dummy.getIMC());
		Assert.assertEquals(1, id0);
		this.manager.remove(id0);
		long id1 = this.manager.add(Dummy.getIMC());
		Assert.assertEquals(1, id1);
	}
	
	@Test
	public void testRouting() throws ImInitializeException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test routing."));
		Set<SupportedMessageType> types = new HashSet<>();
		SupportedMessageType type = SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0001);
		types.add(type);
		types.add(SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0003));
	
		Assert.assertEquals(0,type.getVendorId());
		Assert.assertEquals(1,type.getType());
		
		long id0 = this.manager.add(Dummy.getIMCwithMessageSupport(types));
		Assert.assertEquals(1, id0);
		
		Set<Long> ids = this.router.findRecipientIds(type.getVendorId(), type.getType());
		Assert.assertEquals(new Long(id0),ids.iterator().next());
		
		this.manager.remove(id0);
		
		ids = this.router.findRecipientIds(type.getVendorId(), type.getType());
		if(!ids.isEmpty()){
			Assert.fail();
		}
	}
	
	@Test
	public void testReservAdditinalIdAndExclRouting() throws ImInitializeException, TncException{
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test routing and exclusive delivery."));
		Set<SupportedMessageType> types = new HashSet<>();
		SupportedMessageType type = SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0001);
		types.add(type);
		types.add(SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(0x0003));
	
		Assert.assertEquals(0,type.getVendorId());
		Assert.assertEquals(1,type.getType());
		
		IMC imc = Dummy.getIMCwithMessageSupport(types);
		long id0 = this.manager.add(imc);
		Assert.assertEquals(1, id0);
		
		this.manager.reserveAdditionalId(imc);
		
		Long l = this.router.findExclRecipientId(new Long(2), type.getVendorId(), type.getType());
		Assert.assertEquals(new Long(id0),l);
		
		this.manager.remove(id0);
		
		l = this.router.findExclRecipientId(new Long(2), type.getVendorId(), type.getType());
		if(l != null){
			Assert.fail();
		}
	}
	
	
}
