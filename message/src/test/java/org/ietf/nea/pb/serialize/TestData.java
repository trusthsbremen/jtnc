package org.ietf.nea.pb.serialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageBuilderIetf;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class TestData {

//	byte[] header = new byte[] {0x02, 0x00, 0x00,0x01,
//								0x00, 0x00, 0x00, 0x28};
//	byte[] messageheader = new byte[] { (byte)0x80, 0x00,0x00,0x00,
//										0x00,0x00,0x00,0x01,
//										0x00, 0x00,0x00,0x20};
//	byte[] imMessageHeader = new byte[] { 0x00, 0x00, 0x00, 0x00,
//										  0x00,0x00, 0x00,0x01,
//										  0x00,0x01, (byte)0xff,(byte)0xff};
//	byte[] imMessageBody = new byte[] {0x00,0x00,0x01,0x00,0x00,0x00,0x03,0x02};
	

	byte[] mixedBatch = new byte[]{2, -128, 0, 1, 0, 0, 0, -128, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0,
			0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1, -1, 80, 87, 78, 68, 0,
			0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 16, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
			0, 7, 0, 0, 0, 76, 0, 0, 0, 57, 68, 111, 110, 39, 116, 32, 101, 
			118, 101, 114, 32, 116, 97, 107, 101, 32, 105, 110, 116, 105, 109,
			97, 116, 101, 32, 112, 105, 99, 116, 117, 114, 101, 115, 32, 119, 
			105, 116, 104, 32, 121, 111, 117, 114, 32, 109, 111, 98, 105, 108, 
			101, 32, 112, 104, 111, 110, 101, 46, 2, 101, 110};

	// TODO PaMessage is only a placeholder and nor real data. This will be changed as soon as the IMC message part is finished.
	byte[] imBatch = new byte[]{2, -128, 0, 1, 0, 0, 0, 36, -128, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1, -1, 80, 87, 78, 68};

	byte[] reasonBatch = new byte[]{2, -128, 0, 1, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 76,
			0, 0, 0, 57, 68, 111, 110, 39, 116, 32, 101, 118, 101, 114, 32, 116,
			97, 107, 101, 32, 105, 110, 116, 105, 109, 97, 116, 101, 32, 112, 105,
			99, 116, 117, 114, 101, 115, 32, 119, 105, 116, 104, 32, 121, 111, 117,
			114, 32, 109, 111, 98, 105, 108, 101, 32, 112, 104, 111, 110, 101, 
			46, 2, 101, 110};
	
	byte[] recommendationBatch = new byte[]{2, -128, 0, 1, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 16, 0, 0, 0, 1};
	
	public ByteArrayInputStream getBatchWithImAsStream() throws IOException{
		return new ByteArrayInputStream(imBatch);
	}
	
	public ByteArrayInputStream getBatchWithReasonAsStream() throws IOException{
		return new ByteArrayInputStream(reasonBatch);
	}
	
	public ByteArrayInputStream getBatchWithRecommendationAsStream() throws IOException{
		return new ByteArrayInputStream(recommendationBatch);
	}
	
	public ByteArrayInputStream getBatchWithMixedAsStream() throws IOException{
		return new ByteArrayInputStream(mixedBatch);
	}
	
	public PbBatch getBatchWithImMessage(){
		
		
		PbMessageImFlagsEnum[] imFlags = new PbMessageImFlagsEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<PbMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	public PbBatch getBatchWithInvalidImMessage(){
		
		PbMessageImFlagsEnum[] imFlags = new PbMessageImFlagsEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<PbMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	
	public PbBatch getBatchWithAccessRecommendation(){
		
		List<PbMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createAccessRecommendation(PbMessageAccessRecommendationEnum.ALLOWED));
		
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	public PbBatch getBatchWithReasonString(){
		
		List<PbMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createReasonString("Don't ever take intimate pictures with your mobile phone.", "en"));
		
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	public PbBatch getBatchWithMixedMessages(){
		
		PbMessageImFlagsEnum[] imFlags = new PbMessageImFlagsEnum[0];
		long subVendorId = IETFConstants.IETF_PEN_VENDORID;
		long subType = 1L;
		short collectorId = 1;
		short validatorId = (short)0xFFFF;
		byte[] message = "PWND".getBytes(Charset.forName("US-ASCII"));
		List<PbMessage> messages = new ArrayList<>();
		messages.add(PbMessageFactoryIetf.createIm(imFlags, subVendorId, subType, collectorId, validatorId, message));
		messages.add(PbMessageFactoryIetf.createAccessRecommendation(PbMessageAccessRecommendationEnum.ALLOWED));
		messages.add(PbMessageFactoryIetf.createReasonString("Don't ever take intimate pictures with your mobile phone.", "en"));
		
		return PbBatchFactoryIetf.createClientData(messages);
	}
	
	public PbBatch getInvalidImMessage() throws ValidationException{
		PbMessageBuilderIetf builder = new PbMessageBuilderIetf();
		builder.setFlags(new PbMessageFlagsEnum[0]);
		builder.setVendorId(IETFConstants.IETF_PEN_VENDORID);
		builder.setType(PbMessageTypeEnum.IETF_PB_PA.messageType());
		builder.setValue(PbMessageValueBuilderIetf.createImValue(new PbMessageImFlagsEnum[0], 0, 0, (short)0xFFFF, (short)1, new byte[]{ -128, 34, 12}));

		List<PbMessage> messages = new ArrayList<>();
		messages.add(builder.toMessage());
		
		
		return PbBatchFactoryIetf.createClientData(messages);
	}
}
