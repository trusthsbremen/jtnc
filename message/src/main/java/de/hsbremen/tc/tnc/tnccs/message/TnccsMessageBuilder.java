package de.hsbremen.tc.tnc.tnccs.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface TnccsMessageBuilder {

	public abstract TnccsMessage toMessage() throws RuleException;

	public abstract TnccsMessageBuilder clear();
}