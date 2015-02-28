package org.ietf.nea.pa.serialize;

import java.util.Arrays;

import org.ietf.nea.ByteArrayHelper;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueInstalledPackages;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.message.PaMessage;
import org.ietf.nea.pa.serialize.writer.bytebuffer.PaWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class WriterByteBufferTest {


	TestData message;
	ImWriter<ImMessage> bs;
	
	@Before
	public void setUp(){
		message = new TestData();
		bs = PaWriterFactory.createProductionDefault();
	}
	
	@Test
	public void serializePaMessageWithAssessmentResult() throws ValidationException{
		
		PaMessage b = message.getMessageWithAssessmentResult();
	
		
		ByteBuffer buffer = new DefaultByteBuffer(b.getHeader().getLength());
		try{
			bs.write(b, buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = buffer.read((int)(buffer.bytesWritten()-buffer.bytesRead()));
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT.attributeType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(PaAttributeAssessmentResultEnum.MINOR_DIFFERENCES.number(),ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 21, 24)));	
		
		
	}
	@Test
	
	public void serializePaMessageWithNumericVersion() throws ValidationException{

		
		PaMessage b = message.getMessageWithNumericVersion();
		ByteBuffer buffer = new DefaultByteBuffer(b.getHeader().getLength());
		try{
			bs.write(b, buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = buffer.read((int)(buffer.bytesWritten()-buffer.bytesRead()));
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(258,  ByteArrayHelper.toLong((Arrays.copyOfRange(serialized, 29, 32))));
	
		
	}
	@Test
	public void serializePaMessageWithInstalledPackages() throws ValidationException{
			
		PaMessage b = message.getMessageWithInstalledPackages();
		ByteBuffer buffer = new DefaultByteBuffer(b.getHeader().getLength());
		try{
			bs.write(b, buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = buffer.read((int)(buffer.bytesWritten()-buffer.bytesRead()));
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(2,  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 23, 24)));
		Assert.assertEquals(((PaAttributeValueInstalledPackages)b.getAttributes().get(0).getValue()).getPackages().get(0).getPackageNameLength(),  ByteArrayHelper.toInt(Arrays.copyOfRange(serialized, 24, 25)));
		
	}
	
	@Test
	public void serializePaMessageWithAttributeRequest() throws ValidationException{
			
		PaMessage b = message.getMessageWithAttributeRequest();
		ByteBuffer buffer = new DefaultByteBuffer(b.getHeader().getLength());
		try{
			bs.write(b, buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = buffer.read((int)(buffer.bytesWritten()-buffer.bytesRead()));
		
		System.out.println(Arrays.toString(serialized));
		
		Assert.assertEquals(0x01,serialized[0]);
		Assert.assertEquals(PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 13, 16)));
		Assert.assertEquals(((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getVendorId(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 21, 24)));
		Assert.assertEquals(((PaAttributeValueAttributeRequest)b.getAttributes().get(0).getValue()).getReferences().get(0).getType(),  ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 25, 28)));
	}
	
	@Test
	public void serializePaMessageWithMixedAttributes() throws ValidationException{
			
		PaMessage b = message.getMessageWithMixedAttributes();
		ByteBuffer buffer = new DefaultByteBuffer(b.getHeader().getLength());
		
		try{
			bs.write(b, buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = buffer.read((int)(buffer.bytesWritten()-buffer.bytesRead()));
		
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
