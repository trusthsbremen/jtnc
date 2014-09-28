package org.ietf.nea.pb.message;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValue;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValueBuilder;

public class PbMessageValueRemediationParameterUriBuilderIetf implements PbMessageValueRemediationParameterUriBuilder{

    private URI uri;
    
    public PbMessageValueRemediationParameterUriBuilderIetf(){
    	this.uri = null;
    }

	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.message.PbMessageValueRemediationParameterUriBuilder#setUri(java.lang.String)
	 */
	@Override
	public void setUri(String uri) throws ValidationException {
		NoZeroString.check(uri);
		
		try{
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new ValidationException("URI syntax not valid.",e, true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
	}

	@Override
	public TnccsMessageSubValue toValue() throws ValidationException {
		if( uri == null){
				throw new IllegalStateException("A message value has to be set.");
		}
		
		return new PbMessageValueRemediationParameterUri(uri);
	}

	@Override
	public TnccsMessageSubValueBuilder clear() {

		return new PbMessageValueRemediationParameterUriBuilderIetf();
	}

}
