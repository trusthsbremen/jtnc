package org.ietf.nea.pa.serialize;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

public class WriterTest {


	TestData message;
	ImWriter<PaMessage> bs;
	
	@Before
	public void setUp(){
		message = new TestData();
		bs = PaWriterFactory.createProductionDefault();
	}
	
	@Test
	public void serializePaMessageWithAssessmentResult() throws ValidationException{
		
		PaMessage b = message.getMessageWithAssessmentResult();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES.number(),ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 21, 24)));	
		
		
	}
	@Test
	
	public void serializePaMessageWithNumericVersion() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PaMessage b = message.getMessageWithNumericVersion();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(258,  ByteArrayHelper.toLong((Arrays.copyOfRange(serialized, 29, 32))));
	
		
	}
	@Test
	public void serializePaMessageWithInstalledPackages() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
			
		PaMessage b = message.getMessageWithInstalledPackages();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.toByteArray();
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(2,  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 23, 24)));
		Assert.assertEquals(((PaAttributeValueInstalledPackages)b.getAttributes().get(0).getValue()).getPackages().get(0).getPackageNameLength(),  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 24, 25)));
		
	}
	
	@Test
	public void serializePaMessageWithAttributeRequest() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
			
		PaMessage b = message.getMessageWithAttributeRequest();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.toByteArray();
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getVendorId(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 21, 24)));
		Assert.assertEquals(((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 25, 28)));
	}
	
	@Test
	public void serializePaMessageWithMixedAttributes() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
			
		PaMessage b = message.getMessageWithMixedAttributes();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.toByteArray();
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES.number(),ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 21, 24)));	
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 29, 32)));
		Assert.assertEquals(258,  ByteArrayHelper.toLong((Arrays.copyOfRange(serialized, 45, 48))));
		
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 57, 60)));
		Assert.assertEquals(2,  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 67, 68)));
		Assert.assertEquals(((PaAttributeValueInstalledPackages)b.getAttributes().get(2).getValue()).getPackages().get(0).getPackageNameLength(),  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 68, 69)));
		
	}
	
}
