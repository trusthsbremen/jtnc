package org.ietf.nea.pb.batch;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.validate.rules.BatchDirectionAndType;
import org.ietf.nea.pb.validate.rules.BatchDirectionality;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;
import org.ietf.nea.pb.validate.rules.BatchType;
import org.ietf.nea.pb.validate.rules.BatchVersion;

import de.hsbremen.tc.tnc.IETFConstants;

public class PbBatchBuilderIetf implements PbBatchBuilder {

	private static final byte SUPPORTED_VERSION = 2;
	private static final int RESERVED = 0;  // defined in RFC5793 
	private byte version;
	private PbBatchTypeEnum type;
	private PbBatchDirectionalityEnum direction;
	private List<PbMessage> messages;
	private long batchLength;
	
	
	
	public PbBatchBuilderIetf(){
		this.version = SUPPORTED_VERSION;
		this.type = null;
		this.type =  null;
		this.messages = new LinkedList<>();
		this.batchLength = PbMessageTlvFixedLength.BATCH.length();
	}
	
	@Override
	public PbBatchBuilder setVersion(byte version) throws RuleException {
		BatchVersion.check(version, SUPPORTED_VERSION);
		
		this.version = version;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.batch.PbBatchBuilder#setBatchDirection(org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum)
	 */
	@Override
	public PbBatchBuilder setDirection(byte direction) throws RuleException{
		
		BatchDirectionality.check(direction);
		PbBatchDirectionalityEnum tempDir = PbBatchDirectionalityEnum.fromDirectionality(direction);
		
		if(type != null){
			BatchDirectionAndType.check(tempDir, type);
		}

		this.direction = tempDir;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.batch.PbBatchBuilder#setBatchType(org.ietf.nea.pb.batch.enums.PbBatchTypeEnum)
	 */
	@Override
	public PbBatchBuilder setType(byte type) throws RuleException{

		BatchType.check(type);
		
		PbBatchTypeEnum tempType = PbBatchTypeEnum.fromType(type);
	
		if(direction != null){
			BatchDirectionAndType.check(direction, tempType);
		}
		
		this.type = tempType;
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.batch.PbBatchBuilder#addMessage(org.ietf.nea.pb.message.PbMessage)
	 */
	@Override
	public PbBatchBuilder addMessage(PbMessage message){
		if(message != null){
			this.addMessageAndCheckLength(message);
		}
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.batch.PbBatchBuilder#addAllMessages(java.util.List)
	 */
	@Override
	public PbBatchBuilder addMessages(List<PbMessage> messages){
		if(messages != null){
			for (PbMessage pbMessage : messages) {
				this.addMessage(pbMessage);
			}
		}
		
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.batch.TnccsBatchBuilder#toBatch()
	 */
	@Override
	public  PbBatch toBatch() throws RuleException{
		if(direction == null){
			throw new IllegalStateException("Direction must be set first.");
		}
		if(type == null){
			throw new IllegalStateException("Type must be set first.");
		}
		
		BatchResultWithoutMessageAssessmentResult.check(type, messages);
		
		PbBatch batch = new PbBatch(version,direction, RESERVED, type, batchLength, messages);
		
		return batch;
	}
	
	
	private void addMessageAndCheckLength(PbMessage message){
			long messageLength = message.getLength();
			if(messageLength > 0 && (IETFConstants.IETF_MAX_LENGTH - messageLength) < this.batchLength){
				throw new ArithmeticException("Batch size is to large.");
			}
			this.batchLength += messageLength;
			this.messages.add(message);
	}

	@Override
	public PbBatchBuilder clear() {
		return new PbBatchBuilderIetf();
	}


}
