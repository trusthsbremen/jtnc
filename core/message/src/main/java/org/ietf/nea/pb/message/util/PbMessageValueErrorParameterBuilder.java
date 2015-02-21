package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueErrorParameterBuilder {

	public abstract AbstractPbMessageValueErrorParameter toObject() throws RuleException;
	
	public abstract PbMessageValueErrorParameterBuilder newInstance();
}
