package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.validate.rules.BatchDirectionAndType;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;
import org.ietf.nea.pb.validate.rules.BatchVersion;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbBatchValidator implements TnccsValidator<PbBatch> {

	private final TnccsValidator<PbMessage> messageValidator;
	private static final byte supportedVersion = 2;
	
	public PbBatchValidator() {
    	this(PbMessageValidatorFactroy.createDefault());
    }
	
    public PbBatchValidator(TnccsValidator<PbMessage> messageValidator) {
    	this.messageValidator = messageValidator;
    }
    
    /* (non-Javadoc)
     * @see de.hsbremen.jtnc.message.validator.TnccsHeaderValidator#validate(de.hsbremen.jtnc.message.tnccs.TnccsBatch)
     */
    @Override
    public void validate(final PbBatch batch) throws ValidationException{
        BatchVersion.check(batch.getVersion(), supportedVersion);
    	BatchDirectionAndType.check(batch.getDirectionality(), batch.getType());
    	BatchResultWithoutMessageAssessmentResult.check(batch.getType(), batch.getMessages());
    	for (PbMessage m : batch.getMessages()) {
			messageValidator.validate(m);
		}
    }
}
