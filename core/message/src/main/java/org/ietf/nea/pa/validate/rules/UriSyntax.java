package org.ietf.nea.pa.validate.rules;

import java.net.URI;
import java.net.URISyntaxException;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;

public class UriSyntax {

	public static void check(final String uri) throws RuleException{
		NotNull.check("URI string cannot be null.", uri);
		try {
			new URI(uri);
		} catch (URISyntaxException e) {
			throw new RuleException("URI syntax not valid.",e, false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.URI_SYNTAX_NOT_VALID.number(),uri);
		}
		
		
	}
	
}
