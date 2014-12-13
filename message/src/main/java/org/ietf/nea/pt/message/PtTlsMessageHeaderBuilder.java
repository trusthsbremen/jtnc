package org.ietf.nea.pt.message;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.t.message.TransportMessageHeaderBuilder;

public interface PtTlsMessageHeaderBuilder extends TransportMessageHeaderBuilder {

	public abstract PtTlsMessageHeaderBuilder setVendorId(long vendorId)
			throws RuleException;

	public abstract PtTlsMessageHeaderBuilder setType(long type)
			throws RuleException;

	public abstract PtTlsMessageHeaderBuilder setLength(long length)
			throws RuleException;

	public abstract PtTlsMessageHeaderBuilder setIdentifier(long identifier)
			throws RuleException;
}