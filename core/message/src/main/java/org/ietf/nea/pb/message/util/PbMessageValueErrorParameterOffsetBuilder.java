package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PbMessageValueErrorParameterOffsetBuilder extends PbMessageValueErrorParameterBuilder {

	public abstract PbMessageValueErrorParameterOffsetBuilder setOffset(long offset)
			throws RuleException;
	
}
