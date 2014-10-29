package org.ietf.nea.pb.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageHeaderBuilder;

public interface PbMessageHeaderBuilder extends TnccsMessageHeaderBuilder {

	public abstract PbMessageHeaderBuilder setFlags(byte flags);

	public abstract PbMessageHeaderBuilder setVendorId(long vendorId)
			throws RuleException;

	public abstract PbMessageHeaderBuilder setType(long type)
			throws RuleException;

	public abstract PbMessageHeaderBuilder setLength(long length)
			throws RuleException;
}