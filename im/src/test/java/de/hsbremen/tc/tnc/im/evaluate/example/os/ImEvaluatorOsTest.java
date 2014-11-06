package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImComponentFactory;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.imc.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.enums.ImTypeEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class ImEvaluatorOsTest {
	
	private Map<Long,ImEvaluator> evaluator;
	private static final long IM_ID = 109;  
	private ImSessionContext ctx;
	
	@Before
	public void setUp(){
	
		Dummy.setLogSettings();
		
		this.ctx = Dummy.getSessionContext();
		TnccAdapter adapter = Dummy.getTnccAdapter();
		ImParameter params = new ImParameter();
		try {
			params.setPrimaryId(adapter.reserveAdditionalId());
		} catch (TNCException e) {
			// will never happen with dummy
		}
		params.setUseExclusive(false);
		
		evaluator = new OsImEvaluatorFactory().createUnits(adapter,params);
	}

	@Test
	public void testEvaluate(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test evaluate method."));
		for (Entry<Long,ImEvaluator> entry: evaluator.entrySet()) {
		
			List<ImObjectComponent> components = entry.getValue().evaluate(this.ctx);
			
			for (ImObjectComponent imComponent : components) {
				Assert.assertEquals(imComponent.getCollectorId(), entry.getValue().getId());
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
		
	}
	
	@Test
	public void testHandleRequest(){
		System.out.println(Dummy.getTestDescriptionHead(this.getClass().getSimpleName(),"Test handle request with AttributeRequest."));
		AttributeReference reference = new AttributeReference(IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType());
		List<ImAttribute> attributes = new ArrayList<>();
		try{
			attributes.add(PaAttributeFactoryIetf.createAttributeRequest(reference));
		}catch(RuleException e){
			e.printStackTrace();
		}

		ImObjectComponent component = ImComponentFactory.createObjectComponent((byte)0, IETFConstants.IETF_PEN_VENDORID,ImTypeEnum.IETF_PA_OPERATING_SYSTEM.type(), IM_ID, 0, attributes);
		
		for (Entry<Long,ImEvaluator> entry: evaluator.entrySet()) {
			
			List<ImObjectComponent> components = entry.getValue().handle(component, this.ctx);
			for (ImObjectComponent imComponent : components) {
		
				Assert.assertEquals(imComponent.getCollectorId(), entry.getValue().getId());
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
