package org.ietf.nea.pb.batch;

import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;

import de.hsbremen.tc.tnc.exception.ValidationException;

public class PbBatchFactoryIetf {
	
	public static PbBatch createServerData(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.SDATA, messages);
	}
	public static PbBatch createResult(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.RESULT, messages);
	}
	public static PbBatch createServerRetry(List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.SRETRY, messages);
	}
	
	public static PbBatch createServerClose(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.CLOSE, messages);
	}
	
	public static PbBatch createClientRetry(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CRETRY, messages);
	}
	
	public static PbBatch createClientData(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CDATA, messages);
	}
	
	public static PbBatch createClientClose(final List<PbMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CLOSE, messages);
	}

	private static PbBatch createBatch(PbBatchDirectionalityEnum direction, PbBatchTypeEnum type, List<PbMessage> messages) throws ValidationException{
		if(messages == null){
			throw new NullPointerException("Messages cannot be null.");
		}
		
		PbBatchHeaderBuilderIetf builder = new PbBatchHeaderBuilderIetf();
		try{
			builder.setDirection(direction.toDirectionalityBit());
			builder.setType(type.type());
			
			long l = 0;
			for (PbMessage pbMessage : messages) {
				l += pbMessage.getHeader().getLength();
			}
			
			builder.setLength(l + PbMessageTlvFixedLength.BATCH.length());
			
			BatchResultWithoutMessageAssessmentResult.check(type, messages);
		}catch(RuleException e){
			throw new ValidationException(e.getMessage(), e, ValidationException.OFFSET_NOT_SET);
		}
		PbBatch batch = new PbBatch(builder.toBatchHeader(), messages);

		
		return batch; 
	}
	
}
