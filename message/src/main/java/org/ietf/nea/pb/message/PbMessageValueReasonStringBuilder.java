package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueReasonStringBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueReasonStringBuilder setReasonString(String reasonString)
			throws ValidationException;

	public abstract PbMessageValueReasonStringBuilder setLangCode(String langCode)
			throws ValidationException;

}