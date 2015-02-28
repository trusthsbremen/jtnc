package org.ietf.nea.pb.validate.rules;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;

public class UriSyntax {

	public static void check(final String uri) throws RuleException{
		NotNull.check("URI string cannot be null.", uri);
		try {
			new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuleException("URI syntax not valid.",e, true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
		
		
	}
	
}
