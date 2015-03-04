package org.ietf.nea.pa.validate.rules;

import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class RequestingForbiddenAttributes {

	public static void check(final List<AttributeReferenceEntry> attributes) throws RuleException{
		if(attributes != null){
			for (AttributeReferenceEntry ref : attributes) {
				if( ref.getVendorId() == IETFConstants.IETF_PEN_VENDORID ){
					if( ref.getType() == PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id() ){
						throw new RuleException("The attribute request must not request an attribute with vendor id " + IETFConstants.IETF_PEN_VENDORID + " and attribute type " + PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.id() + ".", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.number(), 
								 IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.toString(), attributes.indexOf(attributes));
					}
					
					if( ref.getType() == PaAttributeTypeEnum.IETF_PA_ERROR.id() ){
						throw new RuleException("The attribute request must not request an attribute with vendor id " + IETFConstants.IETF_PEN_VENDORID + " and attribute type " + PaAttributeTypeEnum.IETF_PA_ERROR.id() + ".", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.number(), 
								IETFConstants.IETF_PEN_VENDORID, PaAttributeTypeEnum.IETF_PA_ERROR.toString(), attributes.indexOf(attributes));
					}
					
				}
			}
		}
		
	}
}
