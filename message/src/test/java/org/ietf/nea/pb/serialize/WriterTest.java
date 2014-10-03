package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;
import org.ietf.nea.pb.serialize.writer.PbWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

public class WriterTest {


	TestData batch;
	TnccsWriter<PbBatch> bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbWriterFactory.createDefault();
	}
	
	@Test
	public void serializePbBatchWithIm() throws RuleException{
		
		PbBatch b = batch.getBatchWithImMessage();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithAccessRecommendation() throws RuleException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		PbBatch b = batch.getBatchWithAccessRecommendation();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.fromNumber(ByteArrayHelper.toShort(Arrays.copyOfRange(serialized, 22, 24))));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithReasonString() throws RuleException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
			
		PbBatch b = batch.getBatchWithReasonString();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.toByteArray();
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 24, (int)(24+((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getStringLength()))));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithMixedMessageTypes() throws RuleException{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
				
		PbBatch b = batch.getBatchWithMixedMessages();
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	    byte [] serialized = out.toByteArray();
	    
	    Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.type(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.fromNumber(ByteArrayHelper.toShort(Arrays.copyOfRange(serialized, 50, 52))));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 68, (int)(68+((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getStringLength()))));
		
		
	}
	
}
