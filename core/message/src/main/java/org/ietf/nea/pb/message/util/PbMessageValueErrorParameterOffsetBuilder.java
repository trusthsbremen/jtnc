package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueErrorParameterOffsetBuilder extends PbMessageValueErrorParameterBuilder {

	public abstract PbMessageValueErrorParameterOffsetBuilder setOffset(long offset)
			throws RuleException;
	
}
