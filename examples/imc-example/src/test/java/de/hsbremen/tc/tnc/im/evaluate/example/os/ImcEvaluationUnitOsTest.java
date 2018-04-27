package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

//TODO this test might fail on other platforms than ubuntu which is correct but not helpful needs fixing
@Ignore
public class ImcEvaluationUnitOsTest {
	
	ImcEvaluationUnit evlUnit;
	ImSessionContext ctx;
	
	@BeforeClass
	public static void logSetUp(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		
		evlUnit = new OsImcEvaluationUnit(Dummy.getHandshakeListener());
		ctx = Dummy.getSessionContext();
	}
	
	@Test
	public void testEvaluate(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test evaluate method"));
	
		List<ImAttribute> attributes = evlUnit.evaluate(this.ctx);
		
		if(attributes == null){
			Assert.fail();
		}
		
		Assert.assertTrue((attributes.size() >= 3));
		
		for (ImAttribute imAttribute : attributes) {
			if(imAttribute.getValue() instanceof PaAttributeValueProductInformation){
				System.out.println(imAttribute.getValue().getClass().getCanonicalName() + " : ");
				System.out.println(imAttribute.getValue().toString());
			}else if(imAttribute.getValue() instanceof PaAttributeValueNumericVersion){
				System.out.println(imAttribute.getValue().getClass().getCanonicalName() + " : ");
				System.out.println(imAttribute.getValue().toString());
			}else if(imAttribute.getValue() instanceof PaAttributeValueStringVersion){
				System.out.println(imAttribute.getValue().getClass().getCanonicalName() + " : ");
				System.out.println(imAttribute.getValue().toString());
			}else{
				Assert.fail();
			}
		}
		
	}
	
	@Test
	public void testHandleRequest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle request with AttributeRequest."));
		
		AttributeReferenceEntry reference = new AttributeReferenceEntry(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id());
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			attributes.add(PaAttributeFactoryIetf.createAttributeRequest(reference));
		}catch(ValidationException e){
			e.printStackTrace();
		}

		List<ImAttribute> attributesRespond = evlUnit.handle(attributes,this.ctx);
		
		if(attributesRespond == null){
			Assert.fail();
		}
		
		Assert.assertTrue((attributesRespond.size() >= 1));
		
		for (ImAttribute imAttribute : attributesRespond) {
			if(imAttribute.getValue() instanceof PaAttributeValueProductInformation){
				System.out.println(imAttribute.getClass().getCanonicalName() + " : ");
				System.out.println(((PaAttributeValueProductInformation)imAttribute.getValue()).getName());
			}else if(imAttribute.getValue() instanceof PaAttributeValueNumericVersion){
				System.out.println(imAttribute.getClass().getCanonicalName() + " : ");
				System.out.println(((PaAttributeValueNumericVersion)imAttribute.getValue()).getMinorVersion());
			}else if(imAttribute.getValue() instanceof PaAttributeValueStringVersion){
				System.out.println(imAttribute.getClass().getCanonicalName() + " : ");
				System.out.println(((PaAttributeValueStringVersion)imAttribute.getValue()).getVersionNumber());
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
