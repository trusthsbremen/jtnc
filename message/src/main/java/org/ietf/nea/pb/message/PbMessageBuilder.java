package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageBuilder;

public interface PbMessageBuilder extends TnccsMessageBuilder{

	public abstract PbMessageBuilder setFlags(byte flags);

	public abstract PbMessageBuilder setVendorId(long vendorId) throws RuleException;

	public abstract PbMessageBuilder setType(long type) throws RuleException;

	public abstract PbMessageBuilder setValue(AbstractPbMessageValue value);

}