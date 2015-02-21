package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueExperimentalBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueExperimentalBuilder setMessage(String message)
			throws RuleException;

}