package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueAssessmentResultBuilder extends TnccsMessageValueBuilder{

	/**
	 * @param result the result to set
	 * @throws RuleException 
	 */
	public abstract PbMessageValueAssessmentResultBuilder setResult(long result) throws RuleException;

}