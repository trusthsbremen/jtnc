package org.ietf.nea.pb.message.util;

import org.ietf.nea.exception.RuleException;

public interface PbMessageValueRemediationParameterUriBuilder extends PbMessageValueRemediationParameterBuilder{

	/**
	 * @param uri the uri to set
	 * @throws RuleException 
	 */
	public abstract  PbMessageValueRemediationParameterUriBuilder setUri(String uri) throws RuleException;

}