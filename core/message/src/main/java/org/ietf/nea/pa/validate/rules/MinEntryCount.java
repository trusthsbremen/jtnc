package org.ietf.nea.pa.validate.rules;

import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class MinEntryCount {

	public static void check(final byte minSize, final List<?> entries) throws RuleException{
	
		if(entries == null || entries.size() < minSize){
			throw new RuleException("List has an invalid length for its type.",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.LENGTH_TO_SHORT.number(),(entries == null) ? 0 : entries.size());
		}
		
	}
}
