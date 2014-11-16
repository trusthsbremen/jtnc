package org.ietf.nea.pb.serialize;

import java.io.InputStream;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchContainer;
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
	TnccsReader<PbBatchContainer> bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbReaderFactory.createProductionDefault();
	}
	
	@Test
	public void deserializePbBatchWithIm(){

		PbBatchContainer bc = null;
		try{
			InputStream in = batch.getBatchWithImAsStream();
			bc = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
	}
	
	@Test
	public void deserializePbBatchWithReason(){

		PbBatchContainer bc = null;
		try{
			InputStream in = batch.getBatchWithReasonAsStream();
			bc = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
	}
	
	@Test
	public void deserializePbBatchWithRecommendation(){

		PbBatchContainer bc = null;
		try{
			InputStream in = batch.getBatchWithRecommendationAsStream();
			bc = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);

	}
	
	@Test
	public void deserializePbBatchWithMixedMessageTypes(){

		PbBatchContainer bc = null;
		try{
			InputStream in = batch.getBatchWithMixedAsStream();
			bc = bs.read(in, -1);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.messageType(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType(), b.getMessages().get(1).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType(), b.getMessages().get(2).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(2).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
		
	}
	
}
