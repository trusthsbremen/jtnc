package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageBuilder;

public interface PbMessageBuilder extends TnccsMessageBuilder{

	public abstract PbMessageBuilder setFlags(byte flags);

	public abstract PbMessageBuilder setVendorId(long vendorId) throws ValidationException;

	public abstract PbMessageBuilder setType(long type) throws ValidationException;

	public abstract PbMessageBuilder setValue(AbstractPbMessageValue value);

}