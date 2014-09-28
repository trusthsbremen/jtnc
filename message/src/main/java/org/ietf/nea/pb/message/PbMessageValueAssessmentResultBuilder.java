package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAssessmentResultBuilder extends TnccsMessageValueBuilder{

	/**
	 * @param result the result to set
	 * @throws ValidationException 
	 */
	public abstract PbMessageValueAssessmentResultBuilder setResult(long result) throws ValidationException;

}