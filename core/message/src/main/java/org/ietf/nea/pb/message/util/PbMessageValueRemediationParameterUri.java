package org.ietf.nea.pb.message.util;

import java.net.URI;

public class PbMessageValueRemediationParameterUri extends AbstractPbMessageValueRemediationParameter{
  
    private final URI remediationUri; //variable length


	PbMessageValueRemediationParameterUri(final long length, final URI remediationUri) {
		super(length);
		this.remediationUri = remediationUri;
	}

	/**
	 * @return the remediationUri
	 */
	public URI getRemediationUri() {
		return this.remediationUri;
	}
    
}
