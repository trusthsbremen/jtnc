package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.os.exception.PatternNotFoundException;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;


public class ImvEvaluationUnitOsTest {
	
	ImvEvaluationUnit evlUnit;
	ImSessionContext ctx;
	
	@BeforeClass
	public static void logSetUp(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		evlUnit = new OsImvEvaluationUnit("/validation/os_imv.properties",Dummy.getHandshakeListener());
		ctx = Dummy.getSessionContext();
	}
	
	@Test
	public void testEvaluate(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test evaluate method"));
	
		List<ImAttribute> attributes = evlUnit.evaluate(this.ctx);
		
		if(attributes == null){
			Assert.fail();
		}
		
		Assert.assertTrue((attributes.size() == 1));
		
		for (ImAttribute imAttribute : attributes) {
			if(imAttribute.getValue() instanceof PaAttributeValueAttributeRequest){
				System.out.println(imAttribute.getClass().getCanonicalName() + " : ");
				System.out.println(Arrays.toString(((PaAttributeValueAttributeRequest)imAttribute.getValue()).getReferences().toArray()));
			}else{
				Assert.fail();
			}
		}
		
	}
	
	@Test
	public void testHandleAttributes(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle attributes with os attributes."));
		
		
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			attributes.add(Dummy.getAttributeNumericVersion());
			attributes.add(Dummy.getAttributeProductInformation());
			attributes.add(Dummy.getAttributeStringVersion());
		}catch(ValidationException | NumberFormatException | PatternNotFoundException e){
			e.printStackTrace();
		}

		List<ImAttribute> attributesRespond = evlUnit.handle(attributes,this.ctx);
		
		if(attributesRespond == null){
			Assert.fail();
		}
		
		Assert.assertTrue((attributesRespond.size() == 1));
		
		for (ImAttribute imAttribute : attributesRespond) {
			if(imAttribute.getValue() instanceof PaAttributeValueAssessmentResult){
				System.out.println(imAttribute.getClass().getCanonicalName() + " : ");
				System.out.println(((PaAttributeValueAssessmentResult)imAttribute.getValue()).getResult().toString());
			}else{
				Assert.fail();
			}
		}
		
	}
	
//	@Test
//	public void testCircle(){
//		for(int i = 0; i < 4; i++){
//			this.testEvaluate();
//			try {
//				Thread.currentThread().sleep(1000);
//			} catch (InterruptedException e) {
//				System.out.println("sleep " + i);
//			}
//		}
//	}
	
}
