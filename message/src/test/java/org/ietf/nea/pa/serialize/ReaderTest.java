package org.ietf.nea.pa.serialize;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.serialize.reader.stream.PaReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImReader;

public class ReaderTest {


	TestData data;
	ImReader<ImMessageContainer> bs;
	
	@Before
	public void setUp(){
		TestData.setLogSettings();
		data = new TestData();
		bs = PaReaderFactory.createProductionDefault();
	}
	
	@Test
	public void deserializePaMessageWithAssessmentResult(){

		ImMessageContainer mc = null;
		
		try{
			byte[] msg = data.getMessageWithAssessmentResultAsByteArray();
			InputStream in = new ByteArrayInputStream(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES, ((PaAttributeValueAssessmentResult)b.getAttributes().get(0).getValue()).getResult());
	}
	
	@Test
	public void deserializePaMessageWithNumericVersion(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithNumericVersionAsByteArray();
			InputStream in = new ByteArrayInputStream(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)b.getAttributes().get(0).getValue()).getMinorVersion());
	}
	
	@Test
	public void deserializePaMessageWithInstalledPackages(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithInstalledPackagesAsByteArray();
			InputStream in = new ByteArrayInputStream(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals("1.7.0_40", ((PaAttributeValueInstalledPackages)b.getAttributes().get(0).getValue()).getPackages().get(1).getPackageVersion());

	}
	
	@Test
	public void deserializePaMessageWithAttributeRequest(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithAttributeRequestAsByteArray();
			InputStream in = new ByteArrayInputStream(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_STRING_VERSION.attributeType(), ((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getType());

	}
	
	@Test
	public void deserializePaMessageWithMixedAttributes(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithMixedAttributesAsByteArray();
			InputStream in = new ByteArrayInputStream(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		List<PaAttribute> attributes = new ArrayList<>(b.getAttributes()); 
		Collections.sort(attributes, new Comparator<PaAttribute>(){

			@Override
			public int compare(PaAttribute o1, PaAttribute o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getHeader().getAttributeType() - o2.getHeader().getAttributeType());
					
			}
			
		});
		
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(), attributes.get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)attributes.get(0).getValue()).getMinorVersion());

		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(), attributes.get(1).getHeader().getAttributeType());
		Assert.assertEquals("1.7.0_40", ((PaAttributeValueInstalledPackages)attributes.get(1).getValue()).getPackages().get(1).getPackageVersion());
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), attributes.get(2).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES, ((PaAttributeValueAssessmentResult)attributes.get(2).getValue()).getResult());
	}
	
}
