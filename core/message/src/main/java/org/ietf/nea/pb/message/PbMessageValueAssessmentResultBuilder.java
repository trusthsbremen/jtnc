package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAssessmentResultBuilder extends TnccsMessageValueBuilder{

	/**
	 * @param result the result to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueAssessmentResultBuilder setResult(long result) throws RuleException;

}