package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLength;
import org.ietf.nea.pa.validate.rules.ProductInformationZeroConstraint;

public class PaAttributeValueProductInformationBuilderIetf implements
	PaAttributeValueProductInformationBuilder {
	
	
	private long length;
	private long vendorId;
	private int productId;
	private String name;
	
	public PaAttributeValueProductInformationBuilderIetf(){
		this.length = PaAttributeTlvFixedLength.PRO_INF.length();
		this.vendorId = 0L;
		this.productId = 0;
		this.name = "";
	}

	@Override
	public void setVendorId(long vendorId) throws RuleException {
		ProductInformationZeroConstraint.check(this.vendorId, this.productId);
		this.vendorId = vendorId;
	}

	@Override
	public void setProductId(int productId) throws RuleException {
		
		ProductInformationZeroConstraint.check(this.vendorId, this.productId);
		this.productId = productId;
	}

	@Override
	public void setName(String name){
		if(name != null){
			this.name = name;
		}
	}

	@Override
	public PaAttributeValueProductInformation toValue(){
		return new PaAttributeValueProductInformation(this.length, this.vendorId, this.productId, this.name);
	}

	@Override
	public PaAttributeValueProductInformationBuilder clear() {

		return new PaAttributeValueProductInformationBuilderIetf();
	}

}
