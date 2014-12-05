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

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.example.os.exception.PatternNotFoundException;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

public class ImvEvaluatorOsTest {
	
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
		
		evaluator = OsImvEvaluatorFactory.getInstance().getEvaluators(adapter,params);
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
			
		
		
	}
	
	@Test
	public void testHandleRequest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle request with os attributes."));
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			attributes.add(Dummy.getAttributeNumericVersion());
			attributes.add(Dummy.getAttributeProductInformation());
			attributes.add(Dummy.getAttributeStringVersion());
		}catch(ValidationException | NumberFormatException | PatternNotFoundException e){
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
