package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueUnknownBuilder extends TnccsMessageValueBuilder{

	public abstract void setMessage(byte[] message)
			throws ValidationException;

}