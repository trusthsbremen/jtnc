package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

public class ImEvaluatorOsTest {
	
	private ImEvaluatorManager evaluator;
	private static final long IM_ID = 109;  
	private ImSessionContext ctx;
	
	@BeforeClass
	public static void logSetUp(){
		Dummy.setLogSettings();
	}
	
	@Before
	public void setUp(){
		
		this.ctx = Dummy.getSessionContext();
		TnccAdapter adapter = Dummy.getTnccAdapter();
		ImParameter params = new ImParameter();
		try {
			params.setPrimaryId(adapter.reserveAdditionalId());
		} catch (TncException e) {
			// will never happen with dummy
		}
		
		evaluator = new OsImcEvaluatorFactory().getEvaluators(adapter,params);
	}

	@Test
	public void testEvaluate(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test evaluate method."));
		
		
			List<ImObjectComponent> components = evaluator.evaluate(this.ctx);
			
			for (ImObjectComponent imComponent : components) {
				List<? extends ImAttribute> attributes = imComponent.getAttributes();
				
				if(attributes == null){
					Assert.fail();
				}
				
				Assert.assertTrue((attributes.size() >= 3));
				
				for (ImAttribute imAttribute : attributes) {
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
			
		
		
	}
	
	@Test
	public void testHandleRequest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle request with AttributeRequest."));
		AttributeReference reference = new AttributeReference(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType());
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			attributes.add(PaAttributeFactoryIetf.createAttributeRequest(reference));
		}catch(ValidationException e){
			e.printStackTrace();
		}

		ImObjectComponent component = ImComponentFactory.createObjectComponent((byte)0, IETFConstants.IETF_PEN_VENDORID,ImTypeEnum.IETF_PA_OPERATING_SYSTEM.type(), IM_ID, 0, attributes);
		
		
			List<ImObjectComponent> params = new ArrayList<>();
			params.add(component);
			List<ImObjectComponent> components = evaluator.handle(params, this.ctx);
			for (ImObjectComponent imComponent : components) {
		
				List<? extends ImAttribute> attributesRespond = imComponent.getAttributes();
				
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
