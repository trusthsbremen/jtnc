package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

public interface PbMessageHeaderBuilder {

	public abstract PbMessageHeaderBuilder setFlags(byte flags);

	public abstract PbMessageHeaderBuilder setVendorId(long vendorId)
			throws RuleException;

	public abstract PbMessageHeaderBuilder setType(long type)
			throws RuleException;

	public abstract PbMessageHeaderBuilder setLength(long length)
			throws RuleException;

	public abstract PbMessageHeader toMessageHeader() throws RuleException;

	public abstract PbMessageHeaderBuilder clear();

}