package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueExperimentalBuilder extends TnccsMessageValueBuilder{

	public abstract void setMessage(String message)
			throws ValidationException;

}