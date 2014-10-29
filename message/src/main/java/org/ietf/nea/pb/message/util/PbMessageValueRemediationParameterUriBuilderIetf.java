package org.ietf.nea.pb.message.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.ietf.nea.pb.validate.rules.NoZeroString;

public class PbMessageValueRemediationParameterUriBuilderIetf implements PbMessageValueRemediationParameterUriBuilder{

	private long length;
    private URI uri;
    
    public PbMessageValueRemediationParameterUriBuilderIetf(){
    	this.length = 0;
    	this.uri = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRemediationParameterUriBuilder#setUri(java.lang.String)
	 */
	@Override
	public PbMessageValueRemediationParameterUriBuilder setUri(String uri) throws RuleException {
		
		NoZeroString.check(uri);
		try{
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuleException("URI syntax not valid.",e, true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
		this.length = this.uri.toString().getBytes().length;
		return this;
	}

	@Override
	public PbMessageValueRemediationParameterUri toValue() throws RuleException {
		
		if( uri == null){
				throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PbMessageValueRemediationParameterUri(this.length, this.uri);
	}

	@Override
	public PbMessageValueRemediationParameterUriBuilder clear() {

		return new PbMessageValueRemediationParameterUriBuilderIetf();
	}

}
