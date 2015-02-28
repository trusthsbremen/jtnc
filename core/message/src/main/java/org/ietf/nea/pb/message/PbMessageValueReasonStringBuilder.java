package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueReasonStringBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueReasonStringBuilder setReasonString(String reasonString)
			throws RuleException;

	public abstract PbMessageValueReasonStringBuilder setLangCode(String langCode)
			throws RuleException;

}