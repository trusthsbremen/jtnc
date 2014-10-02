package org.ietf.nea.pb.message;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

public interface PbMessageValueRemediationParameterUriBuilder extends TnccsMessageSubValueBuilder{

	/**
	 * @param uri the uri to set
	 * @throws RuleException 
	 */
	public abstract  PbMessageValueRemediationParameterUriBuilder setUri(String uri) throws RuleException;

}