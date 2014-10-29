package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueRemediationParameterBuilder {

	public abstract AbstractPbMessageValueRemediationParameter toValue() throws RuleException;
	
	public abstract PbMessageValueRemediationParameterBuilder clear();
}
