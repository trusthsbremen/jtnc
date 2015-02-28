package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public interface PbMessageValueRemediationParameterUriBuilder extends PbMessageValueRemediationParameterBuilder{

	/**
	 * @param uri the uri to set
	 * @throws RuleException 
	 */
	public abstract  PbMessageValueRemediationParameterUriBuilder setUri(String uri) throws RuleException;

}