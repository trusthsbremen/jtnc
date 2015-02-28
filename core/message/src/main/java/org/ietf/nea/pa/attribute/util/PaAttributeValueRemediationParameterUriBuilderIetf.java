package org.ietf.nea.pa.attribute.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;
import org.ietf.nea.pa.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueRemediationParameterUriBuilderIetf implements PaAttributeValueRemediationParameterUriBuilder{

	private long length;
    private URI uri;
    
    public PaAttributeValueRemediationParameterUriBuilderIetf(){
    	this.length = 0;
    	this.uri = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRemediationParameterUriBuilder#setUri(java.lang.String)
	 */
	@Override
	public PaAttributeValueRemediationParameterUriBuilder setUri(String uri) throws RuleException {
		
		NoZeroString.check(uri);
		try{
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuleException("URI syntax not valid.",e, true, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
		this.length = this.uri.toString().getBytes().length;
		return this;
	}

	@Override
	public PaAttributeValueRemediationParameterUri toObject() throws RuleException {
		
		if( uri == null){
				throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PaAttributeValueRemediationParameterUri(this.length, this.uri);
	}

	@Override
	public PaAttributeValueRemediationParameterUriBuilder newInstance() {

		return new PaAttributeValueRemediationParameterUriBuilderIetf();
	}

}
