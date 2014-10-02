package org.ietf.nea.pb.validate.rules;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

public class UriSyntax {

	public static void check(final String uri) throws RuleException{
		if(uri == null){
			throw new NullPointerException("URI string cannot be null.");
		}
		try {
			new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuleException("URI syntax not valid.",e, true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
		
		
	}
	
}
