package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PbMessageValueRemediationParameterBuilder {

	public abstract AbstractPbMessageValueRemediationParameter toObject() throws RuleException;
	
	public abstract PbMessageValueRemediationParameterBuilder newInstance();
}
