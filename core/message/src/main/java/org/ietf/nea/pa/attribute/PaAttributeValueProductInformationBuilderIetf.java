/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.validate.rules.ProductInformationZeroConstraint;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueProductInformationBuilderIetf implements
	PaAttributeValueProductInformationBuilder {
	
	
	private long length;
	private long vendorId;
	private int productId;
	private String name;
	
	public PaAttributeValueProductInformationBuilderIetf(){
		this.length = PaAttributeTlvFixedLengthEnum.PRO_INF.length();
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
	public PaAttributeValueProductInformation toObject(){
		return new PaAttributeValueProductInformation(this.length, this.vendorId, this.productId, this.name);
	}

	@Override
	public PaAttributeValueProductInformationBuilder newInstance() {

		return new PaAttributeValueProductInformationBuilderIetf();
	}

}
