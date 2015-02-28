package org.ietf.nea.pb.batch;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.BatchDirectionAndType;
import org.ietf.nea.pb.validate.rules.BatchDirectionality;
import org.ietf.nea.pb.validate.rules.BatchType;
import org.ietf.nea.pb.validate.rules.BatchVersion;
import org.ietf.nea.pb.validate.rules.CommonLengthLimits;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PbBatchHeaderBuilderIetf implements PbBatchHeaderBuilder{

	private static final byte SUPPORTED_VERSION = 2;
	
	private short version;
	private PbBatchTypeEnum type;
	private PbBatchDirectionalityEnum direction;
	private long batchLength;

	public PbBatchHeaderBuilderIetf(){
		this.version = SUPPORTED_VERSION;
		this.type = null;
		this.direction =  null;
		this.batchLength = PbMessageTlvFixedLengthEnum.BATCH.length();
	}

	@Override
	public PbBatchHeaderBuilder setVersion(short version) throws RuleException {
		BatchVersion.check(version, SUPPORTED_VERSION);
		
		this.version = version;
		
		return this;
	}

	@Override
	public PbBatchHeaderBuilder setDirection(byte direction) throws RuleException{
		
		BatchDirectionality.check(direction);
		PbBatchDirectionalityEnum tempDir = PbBatchDirectionalityEnum.fromDirectionalityBit(direction);
		
		if(type != null){
			BatchDirectionAndType.check(tempDir, type);
		}

		this.direction = tempDir;
		
		return this;
	}

	@Override
	public PbBatchHeaderBuilder setType(byte type) throws RuleException{

		BatchType.check(type);
		
		PbBatchTypeEnum tempType = PbBatchTypeEnum.fromType(type);
	
		if(direction != null){
			BatchDirectionAndType.check(direction, tempType);
		}
		
		this.type = tempType;
		
		return this;
	}

	@Override
	public PbBatchHeaderBuilder setLength(long length) throws RuleException{
		
		CommonLengthLimits.check(length);
		
		this.batchLength = length;
		
		return this;
	}
	
	@Override
	public  PbBatchHeader toObject(){
		if(direction == null){
			throw new IllegalStateException("Direction must be set first.");
		}
		if(type == null){
			throw new IllegalStateException("Type must be set first.");
		}
		
		PbBatchHeader batch = new PbBatchHeader(version, direction, type, batchLength);
		
		return batch;
	}
	
	@Override
	public PbBatchHeaderBuilder newInstance() {
		return new PbBatchHeaderBuilderIetf();
	}

}
