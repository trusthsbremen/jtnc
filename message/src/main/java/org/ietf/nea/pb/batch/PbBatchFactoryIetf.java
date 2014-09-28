package org.ietf.nea.pb.batch;

import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

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

	// TODO what do we do with errors
	private static PbBatch createBatch(PbBatchDirectionalityEnum direction, PbBatchTypeEnum type, List<PbMessage> messages) throws ValidationException{
		if(messages == null){
			throw new NullPointerException("Messages cannot be null.");
		}
		PbBatchBuilderIetf builder = new PbBatchBuilderIetf();
		builder.setDirection(direction.directionality());
		builder.setType(type.type());
		builder.addMessages(messages);
		
		PbBatch batch = null;
		
		batch = builder.toBatch();
		
		return batch; 
	}
	
}
