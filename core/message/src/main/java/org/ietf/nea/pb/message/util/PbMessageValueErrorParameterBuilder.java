package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PbMessageValueErrorParameterBuilder {

	public abstract AbstractPbMessageValueErrorParameter toObject() throws RuleException;
	
	public abstract PbMessageValueErrorParameterBuilder newInstance();
}
