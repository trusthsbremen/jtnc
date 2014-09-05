package org.ietf.nea.pb.validate;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbBatchValidator2 implements TnccsHeaderValidator<PbBatch> {
	private static final class SingletonClient{
		private static final PbBatchValidator2 INSTANCE = PbBatchValidator2.createClientInstance();
	}
	
	private static final class SingletonServer{
		private static PbBatchValidator2 INSTANCE = PbBatchValidator2.createServerInstance();
	}
	
    private byte validVersion = 2;
    private PbBatchDirectionalityEnum validDirection; 
    private List<PbBatchTypeEnum> validTypes;
    
    public static PbBatchValidator2 getInstance(PbBatchDirectionalityEnum batchDirection){
    	
    	switch(batchDirection){
		case TO_PBC:
			 return SingletonClient.INSTANCE;
		case TO_PBS:
			 return SingletonServer.INSTANCE;
    	}
    	throw new IllegalArgumentException("Only TO_PBC and TO_PBS are supported.");
    }
    
    private static PbBatchValidator2 createClientInstance(){
    	List<PbBatchTypeEnum> validTypes = new ArrayList<>();
    	validTypes.add(PbBatchTypeEnum.SDATA);
        validTypes.add(PbBatchTypeEnum.RESULT);
        validTypes.add(PbBatchTypeEnum.CLOSE);
        validTypes.add(PbBatchTypeEnum.SRETRY);
        return new PbBatchValidator2(validTypes, PbBatchDirectionalityEnum.TO_PBC);
    }
    
    private static PbBatchValidator2 createServerInstance(){
    	List<PbBatchTypeEnum> validTypes = new ArrayList<>();
    	validTypes.add(PbBatchTypeEnum.CDATA);
        validTypes.add(PbBatchTypeEnum.CLOSE);
        validTypes.add(PbBatchTypeEnum.CRETRY);
        return new PbBatchValidator2(validTypes, PbBatchDirectionalityEnum.TO_PBS);
    }
    
    private PbBatchValidator2(List<PbBatchTypeEnum> validTypes, PbBatchDirectionalityEnum validDirection){
       this.validTypes = validTypes;
       this.validDirection = validDirection;
    }
    
    /* (non-Javadoc)
     * @see de.hsbremen.jtnc.message.validator.TnccsHeaderValidator#validate(de.hsbremen.jtnc.message.tnccs.TnccsBatch)
     */
    @Override
    public void validate(final PbBatch batch) throws ValidationException{
        this.checkVersion(batch);
        this.checkDirection(batch);
        this.checkBatchType(batch);
        
    }
    
    private void checkVersion(final PbBatch batch) throws ValidationException{
        if(batch.getVersion() != validVersion){
            throw new ValidationException("The version "+batch.getVersion()+" is not supported.",true,PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),Long.toString(batch.getVersion()));
        }
    }
    
    private void checkDirection(final PbBatch batch) throws ValidationException{
        if(batch.getDirectionality() != validDirection){
            throw new ValidationException("The opposite direction is expected.", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),batch.getDirectionality().toString());
        }
    }
    
    private void checkBatchType(final PbBatch batch) throws ValidationException{
        if(!validTypes.contains(batch.getType())){
            throw new ValidationException("The batch type "+batch.getType().toString()+" is not expected.", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),batch.getType().toString());
        }
    }
    
// 	  ToDo Not jet decided.    
//    private void checkMessages(final PbBatch batch){
//    	
//    }
}
