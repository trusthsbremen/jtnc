package org.ietf.nea.pb.serialize;

import java.io.InputStream;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.serialize.reader.PbReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;

public class ReaderTest {


	TestData batch;
	TnccsReader<PbBatch> bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbReaderFactory.createDefault();
	}
	
	@Test
	public void deserializePbBatchWithIm(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithImAsStream();
			b = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
	}
	
	@Test
	public void deserializePbBatchWithReason(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithReasonAsStream();
			b = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
	}
	
	@Test
	public void deserializePbBatchWithRecommendation(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithRecommendationAsStream();
			b = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);

	}
	
	@Test
	public void deserializePbBatchWithMixedMessageTypes(){

		PbBatch b = null;
		try{
			InputStream in = batch.getBatchWithMixedAsStream();
			b = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(1).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(2).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
		
	}
	
}
