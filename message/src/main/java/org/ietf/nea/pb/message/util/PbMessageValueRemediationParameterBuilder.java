package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueRemediationParameterBuilder {

	public abstract AbstractPbMessageValueRemediationParameter toObject() throws RuleException;
	
	public abstract PbMessageValueRemediationParameterBuilder newInstance();
}
