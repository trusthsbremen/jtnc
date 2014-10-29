package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueErrorParameterVersionBuilder extends PbMessageValueErrorParameterBuilder {

	public abstract PbMessageValueErrorParameterVersionBuilder setBadVersion(short version)
			throws RuleException;
	
	public abstract PbMessageValueErrorParameterVersionBuilder setMaxVersion(short version)
			throws RuleException;
	
	public abstract PbMessageValueErrorParameterVersionBuilder setMinVersion(short version)
			throws RuleException;
}
