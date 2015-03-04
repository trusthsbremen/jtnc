package org.ietf.nea.pb.serialize;

import java.util.Arrays;

import org.ietf.nea.ByteArrayHelper;
import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.serialize.writer.bytebuffer.PbWriterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class WriterByteBufferTest {


	TestData batch;
	TnccsWriter<TnccsBatch> bs;
	
	@Before
	public void setUp(){
		batch = new TestData();
		bs = PbWriterFactory.createProductionDefault();
	}
	
	@Test
	public void serializePbBatchWithIm() throws ValidationException{
		
		PbBatch b = batch.getBatchWithImMessage();
		
		ByteBuffer out = new DefaultByteBuffer(b.getHeader().getLength());
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.read((int)(out.bytesWritten() - out.bytesRead()));
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.id(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchResult() throws ValidationException{

		PbBatch b = batch.getBatchResult();
		ByteBuffer out = new DefaultByteBuffer(b.getHeader().getLength());
		
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte[] serialized = out.read((int)(out.bytesWritten() - out.bytesRead()));
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.RESULT.id(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueAccessRecommendation)b.getMessages().get(0).getValue()).getRecommendation(), PbMessageAccessRecommendationEnum.fromNumber(ByteArrayHelper.toShort(Arrays.copyOfRange(serialized, 22, 24))));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithReasonString() throws ValidationException{
			
		PbBatch b = batch.getBatchWithReasonString();
		ByteBuffer out = new DefaultByteBuffer(b.getHeader().getLength());
		
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		byte [] serialized = out.read((int)(out.bytesWritten() - out.bytesRead()));
		
		Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.id(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 24, (int)(24+((PbMessageValueReasonString)b.getMessages().get(0).getValue()).getStringLength()))));
		Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	}
	@Test
	public void serializePbBatchWithMixedMessageTypes() throws ValidationException{
				
		PbBatch b = batch.getBatchWithMixedMessages();
		ByteBuffer out = new DefaultByteBuffer(b.getHeader().getLength());
		
		try{
			bs.write(b, out);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	    byte [] serialized = out.read((int)(out.bytesWritten() - out.bytesRead()));
	    
	    Assert.assertEquals(b.getHeader().getLength(),serialized.length);
	    Assert.assertEquals(0x02,serialized[0]);
		Assert.assertEquals(PbBatchTypeEnum.CDATA.id(),serialized[3]);
		Assert.assertEquals(b.getHeader().getLength(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 4, 8)));

		Assert.assertEquals(((PbMessageValueIm)b.getMessages().get(0).getValue()).getSubType(), ByteArrayHelper.toLong(Arrays.copyOfRange(serialized, 24, 28)));
		Assert.assertEquals(((PbMessageValueReasonString)b.getMessages().get(1).getValue()).getReasonString(), new String(Arrays.copyOfRange(serialized, 52, (int)(52+((PbMessageValueReasonString)b.getMessages().get(1).getValue()).getStringLength()))));
		
		
	}
	
}
