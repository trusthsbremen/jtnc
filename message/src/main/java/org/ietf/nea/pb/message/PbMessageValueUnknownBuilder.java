package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueUnknownBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueUnknownBuilder setMessage(byte[] message)
			throws ValidationException;

	public abstract PbMessageValueUnknownBuilder setOmmittable(boolean ommittable);

}