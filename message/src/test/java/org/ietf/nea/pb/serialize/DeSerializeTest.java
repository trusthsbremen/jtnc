package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class DeSerializeTest {

	
	TestData batch;
	PbBatchSerializer bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbSerializerFactory.createDefault();
	}
	
	@Test
	public void deserializePbBatchWithIm(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithImAsStream();
			b = bs.decode(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,b.getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
	}
	
	@Test
	public void deserializePbBatchWithReason(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithReasonAsStream();
			b = bs.decode(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,b.getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(0).getType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
	}
	
	@Test
	public void deserializePbBatchWithRecommendation(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithRecommendationAsStream();
			b = bs.decode(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,b.getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(0).getType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);
		
	}
	
	@Test
	public void deserializePbBatchWithMixedMessageTypes(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithMixedAsStream();
			b = bs.decode(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,b.getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(1).getType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(2).getType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
		
	}
	
	
	@Test
	public void serializePbBatchWithIm() throws ValidationException{
		
		PbBatch b = batch.getBatchWithImMessage();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			bs.encode(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(b.getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithAccessRecommendation() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PbBatch b = batch.getBatchWithAccessRecommendation();
		try{
			bs.encode(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.fromNumber(ByteArrayHelper.toShort(Arrays.copyOfRange(serialized, 22, 24))));
		Assert.assertEquals(b.getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithReasonString() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
			
		PbBatch b = batch.getBatchWithReasonString();
		try{
			bs.encode(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 24, (int)(24+((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getStringLength()))));
		Assert.assertEquals(b.getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithMixedMessageTypes() throws ValidationException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
				
		PbBatch b = batch.getBatchWithMixedMessages();
		try{
			bs.encode(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	    byte [] serialized = out.toByteArray();
	    
	    Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(b.getLength(),serialized.length);
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.fromNumber(ByteArrayHelper.toShort(Arrays.copyOfRange(serialized, 50, 52))));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 68, (int)(68+((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getStringLength()))));
		
		
	}
}
	
