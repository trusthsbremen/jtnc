package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValueBuilder;

public interface PbMessageValueUnknownBuilder extends TnccsMessageValueBuilder{

	public abstract PbMessageValueUnknownBuilder setMessage(byte[] message)
			throws RuleException;

	public abstract PbMessageValueUnknownBuilder setOmmittable(boolean ommittable);

}