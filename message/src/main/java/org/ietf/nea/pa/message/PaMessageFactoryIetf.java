package org.ietf.nea.pa.message;

import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.ValidationException;

public class PaMessageFactoryIetf {
	
	public static PaMessage createMessage(short version, long identifier, List<PaAttribute> attributes) throws ValidationException{
		if(attributes == null){
			throw new NullPointerException("Attributes cannot be null.");
		}
		
		PaMessageHeaderBuilderIetf builder = new PaMessageHeaderBuilderIetf();
		
		try{
			builder.setVersion(version);
			builder.setIdentifier(identifier);
			
			long l = 0;
			for (PaAttribute attr : attributes) {
				l += attr.getHeader().getLength();
			}

			builder.setLength(l+PaAttributeTlvFixedLengthEnum.MESSAGE.length());
			
		}catch(RuleException e){
			throw new ValidationException(e.getMessage(), e, ValidationException.OFFSET_NOT_SET);
		}
		
		PaMessage msg = new PaMessage(builder.toObject(), attributes);

		return msg; 
	}
	
}
