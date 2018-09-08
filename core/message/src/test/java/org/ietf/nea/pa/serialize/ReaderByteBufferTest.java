package org.ietf.nea.pa.serialize;

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
import org.ietf.nea.pa.serialize.reader.bytebuffer.PaReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImMessageContainer;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class ReaderByteBufferTest {


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
			ByteBuffer in = new DefaultByteBuffer(msg.length); 
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES, ((PaAttributeValueAssessmentResult)b.getAttributes().get(0).getValue()).getResult());
	}
	
	@Test
	public void deserializePaMessageWithNumericVersion(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithNumericVersionAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)b.getAttributes().get(0).getValue()).getMinorVersion());
	}
	
	@Test
	public void deserializePaMessageWithInstalledPackages(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithInstalledPackagesAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.id(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals("1.7.0_40", ((PaAttributeValueInstalledPackages)b.getAttributes().get(0).getValue()).getPackages().get(1).getPackageVersion());

	}
	
	@Test
	public void deserializePaMessageWithAttributeRequest(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithAttributeRequestAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id(), ((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getType());

	}
	
	@Test
	public void deserializePaMessageWithMixedAttributes(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithMixedAttributesAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
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
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(), attributes.get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)attributes.get(0).getValue()).getMinorVersion());

		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.id(), attributes.get(1).getHeader().getAttributeType());
		Assert.assertEquals("1.7.0_40", ((PaAttributeValueInstalledPackages)attributes.get(1).getValue()).getPackages().get(1).getPackageVersion());
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(), attributes.get(2).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES, ((PaAttributeValueAssessmentResult)attributes.get(2).getValue()).getResult());
	}
	
	@Test
	public void deserializeFaultyPaMessageWithMixedAttributes(){

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithFaultyMixedAttributesAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		if(mc.getExceptions().isEmpty()){
			Assert.fail();
		}else{
			ValidationException e = mc.getExceptions().get(0);
			Assert.assertEquals(68,e.getExceptionOffset());
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
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(), attributes.get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)attributes.get(0).getValue()).getMinorVersion());
	
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.id(), attributes.get(1).getHeader().getAttributeType());
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES, ((PaAttributeValueAssessmentResult)attributes.get(1).getValue()).getResult());
	}
	
	@Test(expected=ValidationException.class)
	public void deserializeFaultyPaMessageWithNumericVersion() throws Exception{

		ImMessageContainer mc = null;
		try{
			byte[] msg = data.getMessageWithFaultyNumericVersionAsByteArray();
			ByteBuffer in = new DefaultByteBuffer(msg.length);
			in.write(msg);
			mc = bs.read(in, msg.length);
		}catch(Exception e){
			throw e;
		}
		if(!mc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PaMessage b = (PaMessage)mc.getResult();
		
		Assert.assertEquals(1,b.getHeader().getVersion());
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id(), b.getAttributes().get(0).getHeader().getAttributeType());
		Assert.assertEquals(7, ((PaAttributeValueNumericVersion)b.getAttributes().get(0).getValue()).getMinorVersion());
	}
	
	
}
