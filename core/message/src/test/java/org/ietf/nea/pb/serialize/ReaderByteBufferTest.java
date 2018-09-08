package org.ietf.nea.pb.serialize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.serialize.reader.bytebuffer.PbReaderFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public class ReaderByteBufferTest {


	TestData batch;
	TnccsReader<TnccsBatchContainer> bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbReaderFactory.createProductionDefault();
	}
	
	@Test
	public void deserializePbBatchWithIm(){

		TnccsBatchContainer bc = null;
		try{
			ByteBuffer in = batch.getBatchWithImAsBuffer();
			bc = bs.read(in, -1);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.id(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), 1L);
	}
	
	@Test
	public void deserializePbBatchWithReason(){

		TnccsBatchContainer bc = null;
		try{
			ByteBuffer in = batch.getBatchWithReasonAsBuffer();
			bc = bs.read(in, -1);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.id(), b.getMessages().get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
	}
	
	@Test
	public void deserializePbBatchWithRecommendation(){

		TnccsBatchContainer bc = null;
		try{
			ByteBuffer in = batch.getBatchWithRecommendationAsBuffer();
			bc = bs.read(in, -1);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		List<PbMessage> messages = new ArrayList<>(b.getMessages()); 
		Collections.sort(messages, new Comparator<PbMessage>(){

			@Override
			public int compare(PbMessage o1, PbMessage o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getHeader().getMessageType() - o2.getHeader().getMessageType());
					
			}
			
		});
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBC,((PbBatchHeader)b.getHeader()).getDirectionality());
		
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id(), messages.get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAssessmentResult) messages.get(0).getValue()).getResult(), PbMessageAssessmentResultEnum.COMPLIANT);
		
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.id(),messages.get(1).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueAccessRecommendation)messages.get(1).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.ALLOWED);
		
	}
	
	@Test
	public void deserializePbBatchWithWrongTypeRecommendation(){

		TnccsBatchContainer bc = null;
		try{
			ByteBuffer in = batch.getBatchWithWrongTypeAndRecommendationAsBuffer();
			bc = bs.read(in, -1);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertTrue(b.getMessages().isEmpty());

	}
	
	@Test
	public void deserializePbBatchWithMixedMessageTypes(){

		TnccsBatchContainer bc = null;
		try{
			ByteBuffer in = batch.getBatchWithMixedAsBuffer();
			bc = bs.read(in, -1);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		if(!bc.getExceptions().isEmpty()){
			Assert.fail("Exceptions are present.");
		}
		PbBatch b = (PbBatch)bc.getResult();
		
		List<PbMessage> messages = new ArrayList<>(b.getMessages()); 
		Collections.sort(messages, new Comparator<PbMessage>(){

			@Override
			public int compare(PbMessage o1, PbMessage o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getHeader().getMessageType() - o2.getHeader().getMessageType());
					
			}
			
		});
		
		Assert.assertEquals(PbBatchDirectionalityEnum.TO_PBS,((PbBatchHeader)b.getHeader()).getDirectionality());
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_PA.id(), messages.get(0).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueIm)messages.get(0).getValue()).getSubType(), 1L);
		Assert.assertEquals(PbMessageTypeEnum.IETF_PB_REASON_STRING.id(), messages.get(1).getHeader().getMessageType());
		Assert.assertEquals(((PbMessageValueReasonString)messages.get(1).getValue()).getReasonString(),"Don't ever take intimate pictures with your mobile phone.");
		
	}
	
	@Test(expected=ValidationException.class)
	public void deserializeFaultyPbBatchWithRecommendation() throws Exception{

		try{
			ByteBuffer in = batch.getBatchWithFaultyRecommendationAsBuffer();
			bs.read(in, -1);
		}catch(Exception e){
			if(e instanceof ValidationException){
				Assert.assertEquals(22, ((ValidationException) e).getExceptionOffset());
				throw e;
			}else{
				System.err.println(e.getMessage());
			}
		}

	}
	
	@Test(expected=ValidationException.class)
	public void deserializeFatalyFaultyPbBatchWithRecommendation() throws Exception{

		try{
			ByteBuffer in = batch.getBatchWithFatalyFaultyRecommendationAsBuffer();
			bs.read(in, -1);
		}catch(Exception e){
			if(e instanceof ValidationException){
				Assert.assertEquals(24, ((ValidationException) e).getExceptionOffset());
				throw e;
			}else{
				System.err.println(e.getMessage());
			}
		}

	}
	
}
