package org.ietf.nea.pb.batch;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

public class PbBatchFactoryIetf {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(PbBatchFactoryIetf.class);
	
	public static PbBatch createServerData(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.SDATA, messages);
	}
	public static PbBatch createResult(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.RESULT, messages);
	}
	public static PbBatch createServerRetry(List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.SRETRY, messages);
	}
	
	public static PbBatch createServerClose(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBC, PbBatchTypeEnum.CLOSE, messages);
	}
	
	public static PbBatch createClientRetry(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CRETRY, messages);
	}
	
	public static PbBatch createClientData(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CDATA, messages);
	}
	
	public static PbBatch createClientClose(final List<TnccsMessage> messages) throws ValidationException{
		return createBatch(PbBatchDirectionalityEnum.TO_PBS, PbBatchTypeEnum.CLOSE, messages);
	}

	private static PbBatch createBatch(PbBatchDirectionalityEnum direction, PbBatchTypeEnum type, List<TnccsMessage> messages) throws ValidationException{
		if(messages == null){
			throw new NullPointerException("Messages cannot be null.");
		}
		
		PbBatchHeaderBuilderIetf builder = new PbBatchHeaderBuilderIetf();
		List<PbMessage> filteredMsgs = new LinkedList<>();
		try{
			builder.setDirection(direction.toDirectionalityBit());
			builder.setType(type.type());
			
			long l = 0;
			for (TnccsMessage message : messages) {
				if(message instanceof PbMessage){
					PbMessage pbMessage = (PbMessage) message;
					l += pbMessage.getHeader().getLength();
					filteredMsgs.add(pbMessage);
				}else{
					throw new IllegalArgumentException("Message type " + message.getClass().getCanonicalName() + " not supported. TnccsMessage must be of type "+ PbMessage.class.getCanonicalName() +".");
				}
			}
			
			builder.setLength(l + PbMessageTlvFixedLengthEnum.BATCH.length());
			
			BatchResultWithoutMessageAssessmentResult.check(type, messages);
		}catch(RuleException e){
			throw new ValidationException(e.getMessage(), e, ValidationException.OFFSET_NOT_SET);
		}
		PbBatch batch = new PbBatch(builder.toBatchHeader(), filteredMsgs);

		
		return batch; 
	}
	
}
