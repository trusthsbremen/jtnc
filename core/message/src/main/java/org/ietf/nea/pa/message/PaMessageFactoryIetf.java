package org.ietf.nea.pa.message;

import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.util.NotNull;

public class PaMessageFactoryIetf {
    
	public static PaMessage createMessage(long identifier, List<PaAttribute> attributes) throws ValidationException{
		NotNull.check("Attributes cannot be null.", attributes);
		
		PaMessageHeaderBuilderIetf builder = new PaMessageHeaderBuilderIetf();
		
		try{
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
