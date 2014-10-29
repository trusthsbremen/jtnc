package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

public class ProductInformationZeroConstraint {
	 public static void check(final long productVendorId, final int productId ) throws RuleException {
		 	if(productVendorId == 0 && productId != 0){
	            throw new RuleException("Product information attribute has an invalid value constellation, having a vendor id of "+ productVendorId +" and a product id of " + productId + ".",false,PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PaErrorCauseEnum.INVALID_PRODUCT_ID.number(),Long.toString(productVendorId), Long.toString(productId));
	        }
	 }
}
