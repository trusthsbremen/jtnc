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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.validate.rules.MinEntryCount;
import org.ietf.nea.pa.validate.rules.RequestingForbiddenAttributes;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class PaAttributeValueAttributeRequestBuilderIetf implements
PaAttributeValueAttributeRequestBuilder {
	
	
	private long length;
	private List<AttributeReferenceEntry> references;
	
	public PaAttributeValueAttributeRequestBuilderIetf(){
		this.length = 0;
		this.references = new LinkedList<>();
	}

	@Override
	public PaAttributeValueAttributeRequestBuilder addReferences(AttributeReferenceEntry reference, AttributeReferenceEntry... references) throws RuleException {
		
		List<AttributeReferenceEntry> temp = new ArrayList<>();
		
		if(reference != null){
			temp.add(reference);
		}
		
		if(references != null){
			for (AttributeReferenceEntry attributeReference : references) {
				if(attributeReference != null){
					temp.add(attributeReference);
				}
			}
		}
	
		RequestingForbiddenAttributes.check(temp);	
		
		this.references.addAll(temp);
		this.length = (PaAttributeTlvFixedLengthEnum.ATT_REQ.length() * this.references.size());

		MinEntryCount.check((byte)1, this.references);
		
		return this;
	}

	@Override
	public PaAttributeValueAttributeRequest toObject(){
		
		return new PaAttributeValueAttributeRequest(length, references);
	}

	@Override
	public PaAttributeValueAttributeRequestBuilder newInstance() {

		return new PaAttributeValueAttributeRequestBuilderIetf();
	}

}
