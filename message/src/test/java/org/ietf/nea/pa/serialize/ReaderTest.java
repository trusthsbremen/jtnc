package org.ietf.nea.pa.serialize;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.message.PaMessageContainer;
import org.ietf.nea.pa.serialize.reader.PaReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.m.serialize.ImReader;

public class ReaderTest {


	TestData data;
	ImReader<PaMessageContainer> bs;
	
	@Before
	public void setUp(){
		TestData.setLogSettings();
		data = new TestData();
		bs = PaReaderFactory.createProductionDefault();
	}
	
	@Test
	public void deserializePaMessageWithAssessmentResult(){

		PaMessageContainer mc = null;
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

		PaMessageContainer mc = null;
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

		PaMessageContainer mc = null;
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

		PaMessageContainer mc = null;
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

		PaMessageContainer mc = null;
		try{
			byte[] msg = data.getBatchWithMixedAttributesAsByteArray();
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
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(), b.getAttributes().get(1).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)b.getAttributes().get(1).getValue()).getMinorVersion());
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(), b.getAttributes().get(2).getHeader().getAttributeType());
		Assert.assertEquals("1.7.0_40", ((PaAttributeValueInstalledPackages)b.getAttributes().get(2).getValue()).getPackages().get(1).getPackageVersion());
	}
	
}
