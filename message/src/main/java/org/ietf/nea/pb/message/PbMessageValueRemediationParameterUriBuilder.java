package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

public interface PbMessageValueRemediationParameterUriBuilder extends TnccsMessageSubValueBuilder{

	/**
	 * @param uri the uri to set
	 * @throws ValidationException 
	 */
	public abstract  PbMessageValueRemediationParameterUriBuilder setUri(String uri) throws ValidationException;

}