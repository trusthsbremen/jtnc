package org.ietf.nea.pa.serialize;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.serialize.writer.PaWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.m.serialize.ImWriter;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

public class WriterTest {


	TestData message;
	ImWriter<PaMessage> bs;
	
	@Before
	public void setUp(){
		message = new TestData();
		bs = PaWriterFactory.createProductionDefault();
	}
	
	@Test
	public void serializePaMessageWithAssessmentResult() throws RuleException{
		
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
	
	public void serializePaMessageWithNumericVersion() throws RuleException{

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
	public void serializePaMessageWithInstalledPackages() throws RuleException{

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
	public void serializePaMessageWithMixedAttributes() throws RuleException{

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
