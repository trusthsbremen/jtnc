package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.validate.rules.BatchDirectionAndType;
import org.ietf.nea.pb.validate.rules.BatchVersion;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbBatchValidator implements TnccsValidator<PbBatch> {

    private static final class Singleton{
		private static final PbBatchValidator INSTANCE = new PbBatchValidator();  
	}  
	
	public static PbBatchValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbBatchValidator() {
    	// Singleton
    }
    
    /* (non-Javadoc)
     * @see de.hsbremen.jtnc.message.validator.TnccsHeaderValidator#validate(de.hsbremen.jtnc.message.tnccs.TnccsBatch)
     */
    @Override
    public void validate(final PbBatch batch) throws ValidationException{
    	byte validVersion = 2;
        BatchVersion.check(batch.getVersion(), validVersion);
    	BatchDirectionAndType.check(batch.getDirectionality(), batch.getType());
    }
    
    @Override
	public void addValidator(final Long vendorId, final Long messageType,
			final TnccsValidator<PbBatch> validator) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}

	@Override
	public void removeValidator(final Long vendorId, final Long messageType) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}
}
