package org.ietf.nea.pb.batch;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatchBuilder;

public class PbBatchBuilderIetf implements TnccsBatchBuilder {

	private PbBatchTypeEnum type;
	private PbBatchDirectionalityEnum direction;
	private List<PbMessage> messages;
	private long batchLength;
	
	
	public PbBatchBuilderIetf(){
		this.messages = new LinkedList<>();
		this.batchLength = PbBatch.FIXED_LENGTH;
	}
	
	public PbBatchBuilderIetf setBatchDirection(PbBatchDirectionalityEnum direction){
		
		if(!validDirectionAndType(this.direction,type)){
			throw new IllegalStateException("Current type "+ type.toString() +" can not be used with the supplied direction.");
		}
//		if(type != null && direction != null){
//			if(direction == PbBatchDirectionalityEnum.TO_PBS && (type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SRETRY || type == PbBatchTypeEnum.SDATA)){
//				throw new IllegalStateException("Current type "+type.toString()+" can not be used with the supplied direction.");
//			}
//			if(direction == PbBatchDirectionalityEnum.TO_PBC && (type == PbBatchTypeEnum.CDATA || type == PbBatchTypeEnum.CRETRY)){
//				throw new IllegalStateException("Current type "+ type.toString() +" can not be used with the supplied direction.");
//			}
//		}
		
		this.direction = direction;
		
		return this;
	}
	
	public PbBatchBuilderIetf setBatchType(PbBatchTypeEnum type){

		if(!validDirectionAndType(this.direction,type)){
			throw new IllegalStateException("Current direction "+type.toString()+" can not be used with the supplied type.");
		}
		
//		if(direction != null && type != null){
//			if(direction == PbBatchDirectionalityEnum.TO_PBS && (type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SRETRY || type == PbBatchTypeEnum.SDATA)){
//				throw new IllegalStateException("Current direction "+type.toString()+" can not be used with the supplied type.");
//			}
//	
//			if(direction == PbBatchDirectionalityEnum.TO_PBC && (type == PbBatchTypeEnum.CDATA || type == PbBatchTypeEnum.CRETRY)){
//				throw new IllegalStateException("Current direction "+ type.toString() +" can not be used with the supplied type.");
//			}
//		}
		
		this.type = type;
		
		return this;
	}
	
	public PbBatchBuilderIetf addMessage(PbMessage message){
		if(message != null){
			this.addMessageAndCheckLength(message);
		}
		
		return this;
	}
	
	public PbBatchBuilderIetf addAllMessages(List<PbMessage> messages){
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
	public  PbBatch toBatch(){
		int reserved = 0; // defined in RFC5793 
		if(direction == null){
			throw new IllegalStateException("Direction must be set first.");
		}
		if(type == null){
			throw new IllegalStateException("Type must be set first.");
		}
		
		PbBatch batch = new PbBatch(direction, reserved, type, batchLength, messages);
		
		return batch;
	}
	
	private boolean validDirectionAndType(PbBatchDirectionalityEnum direction, PbBatchTypeEnum type){
		boolean b = true;
		if(direction != null && type != null){
			if(direction == PbBatchDirectionalityEnum.TO_PBS && (type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SRETRY || type == PbBatchTypeEnum.SDATA)){
				b = false;
			}
	
			if(direction == PbBatchDirectionalityEnum.TO_PBC && (type == PbBatchTypeEnum.CDATA || type == PbBatchTypeEnum.CRETRY)){
				b = false;
			}
		}
		return b;
	}
	
	private void addMessageAndCheckLength(PbMessage message){
			long messageLength = message.getLength();
			if(messageLength > 0 && (IETFConstants.IETF_MAX_LENGTH - messageLength) < this.batchLength){
				throw new ArithmeticException("Batch size is to large.");
			}
			this.batchLength += messageLength;
			this.messages.add(message);
	}

}
