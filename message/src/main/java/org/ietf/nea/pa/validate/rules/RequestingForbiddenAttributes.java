package org.ietf.nea.pa.validate.rules;

import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class RequestingForbiddenAttributes {

	public static void check(final List<AttributeReference> attributes) throws RuleException{
		if(attributes != null){
			for (AttributeReference ref : attributes) {
				if( ref.getVendorId() == IETFConstants.IETF_PEN_VENDORID ){
					if( ref.getType() == PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType() ){
						throw new RuleException("The attribute request must not request an attribute with vendor id " + IETFConstants.IETF_PEN_VENDORID + " and attribute type " + PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.attributeType() + ".", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.number(), 
								 Long.toString(IETFConstants.IETF_PEN_VENDORID), PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST.toString(), Long.toString(attributes.indexOf(attributes)));
					}
					
					if( ref.getType() == PaAttributeTypeEnum.IETF_PA_ERROR.attributeType() ){
						throw new RuleException("The attribute request must not request an attribute with vendor id " + IETFConstants.IETF_PEN_VENDORID + " and attribute type " + PaAttributeTypeEnum.IETF_PA_ERROR.attributeType() + ".", false, PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.number(), 
								 Long.toString(IETFConstants.IETF_PEN_VENDORID), PaAttributeTypeEnum.IETF_PA_ERROR.toString(), Long.toString(attributes.indexOf(attributes)));
					}
					
				}
			}
		}
		
	}
}
