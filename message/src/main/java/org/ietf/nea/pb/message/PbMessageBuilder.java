package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageBuilder;

public interface PbMessageBuilder extends TnccsMessageBuilder{

	public abstract void setFlags(byte flags);

	public abstract void setVendorId(long vendorId) throws ValidationException;

	public abstract void setType(long type) throws ValidationException;

	public abstract void setValue(AbstractPbMessageValue value);

}