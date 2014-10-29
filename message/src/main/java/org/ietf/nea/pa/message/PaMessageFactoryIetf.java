package org.ietf.nea.pa.message;

import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.validate.rules.CommonLengthLimits;

public class PaMessageFactoryIetf {
	
	// TODO what do we do with errors
	public static PaMessage createMessage(short version, long identifier, List<PaAttribute> attributes) throws RuleException{
		if(attributes == null){
			throw new NullPointerException("Attributes cannot be null.");
		}
		
		PaMessageHeaderBuilderIetf builder = new PaMessageHeaderBuilderIetf();
		builder.setVersion(version);
		builder.setIdentifier(identifier);
		
		long l = 0;
		for (PaAttribute attr : attributes) {
			l += attr.getHeader().getLength();
		}

		CommonLengthLimits.check(l);
		
		PaMessage msg = new PaMessage(builder.toMessageHeader(), attributes);

		return msg; 
	}
	
}
