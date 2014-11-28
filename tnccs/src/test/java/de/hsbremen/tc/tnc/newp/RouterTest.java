package de.hsbremen.tc.tnc.newp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.newp.route.DefaultImMessageRouter;
import de.hsbremen.tc.tnc.newp.route.ImMessageRouter;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

public class RouterTest {

	private ImMessageRouter router;
	private Random randVID;
	private Random randT;
	private int entryCount;
	private long imId0 = 1;
	private long imId1_0 = 2;
	private long imId1_1 = 4;
	Set<SupportedMessageType> types0;
	Set<SupportedMessageType> types1;
	private SupportedMessageType testType;
	
	@BeforeClass
	public static void logSetup(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
	
		this.testType = SupportedMessageTypeFactory.createSupportedMessageType(2, 5);
		this.entryCount = 7;
		this.randVID = new Random();
		this.randT = new Random();
		
		types0 = new HashSet<>();
		for (int i = 0; i < (entryCount/2) ; i++){
			types0.add(SupportedMessageTypeFactory.createSupportedMessageType(randVID.nextInt(3), randT.nextInt(10)));
			
		}
		types0.add(testType);
		
		types1 = new HashSet<>();
		for (int i = 0; i < entryCount ; i++){
			types1.add(SupportedMessageTypeFactory.createSupportedMessageType(randVID.nextInt(3), randT.nextInt(10)));
		}
		types1.add(testType);
	}
	
	@Test
	public void simpleRoutingTest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test simple routing."));
		this.router = new DefaultImMessageRouter();
		this.router.updateMap(imId0, types0);
		Set<Long> ids = this.router.findRecipientIds(testType.getVendorId(), testType.getType());
		
		System.out.println("Actuall size:" + ids.size());
		if(ids.size() < 1){
			Assert.fail();
		}
		
		if(!ids.contains(imId0)){
			Assert.fail();
		}
	}
	
	@Test
	public void twoImRoutingTest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test routing with to IMC/V."));
		this.router = new DefaultImMessageRouter();
		this.router.updateMap(imId0, types0);
		this.router.updateMap(imId1_0, types1);
		
		Long l = this.router.findExclRecipientId(imId0, testType.getVendorId(), testType.getType());
		System.out.println(l);
		if(l.longValue() != this.imId0){
			Assert.fail();
		}
		
		Set<Long> ids = this.router.findRecipientIds(testType.getVendorId(), testType.getType());
		
		System.out.println("Actuall size:" + ids.size());
		if(ids.size() < 2){
			Assert.fail();
		}
		
		if(!ids.contains(imId0)){
			Assert.fail();
		}
		
		if(!ids.contains(imId1_0)){
			Assert.fail();
		}
		
	}
	
	@Test
	public void routeRemoveTest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test remove IMC/V from map."));
	
		this.router = new DefaultImMessageRouter();
		this.router.updateMap(imId0, types0);
		this.router.updateMap(imId1_0, types1);
		
		Set<Long> ids = this.router.findRecipientIds(testType.getVendorId(), testType.getType());
		
		System.out.println("Actuall size:" + ids.size());
		if(ids.size() < 2){
			Assert.fail();
		}
		
		if(!ids.contains(imId0)){
			Assert.fail();
		}
		
		if(!ids.contains(imId1_0)){
			Assert.fail();
		}
		
		this.router.remove(imId0);
		
		ids = this.router.findRecipientIds(testType.getVendorId(), testType.getType());
		
		System.out.println("Actuall size:" + ids.size());
		if(ids.size() < 1){
			Assert.fail();
		}
		
		if(!ids.contains(imId1_0)){
			Assert.fail();
		}
	}
	
	@Test 
	public void exclRouteTest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test exclusive routing."));
		
		this.router = new DefaultImMessageRouter();
		this.router.updateMap(imId0, types0);
		this.router.updateMap(imId1_0, types1);
		
		Long l = this.router.findExclRecipientId(imId0, testType.getVendorId(), testType.getType());
		System.out.println(l);
		if(l.longValue() != this.imId0){
			Assert.fail();
		}
	}
	
	@Test
	public void additionalIdExclRouteTest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test exclusive routing with additional ID."));
		
		this.router = new DefaultImMessageRouter();
		this.router.updateMap(imId0, types0);
		this.router.updateMap(imId1_0, types1);
		this.router.addExclusiveId(imId1_0, imId1_1);
		
		Long l = this.router.findExclRecipientId(imId1_1, testType.getVendorId(), testType.getType());
		System.out.println(l);
		if(l.longValue() != this.imId1_0){
			Assert.fail();
		}
		
	}
	
}
