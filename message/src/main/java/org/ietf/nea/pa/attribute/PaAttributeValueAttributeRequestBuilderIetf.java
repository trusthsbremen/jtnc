package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.ietf.nea.pa.validate.rules.MinEntryCount;
import org.ietf.nea.pa.validate.rules.RequestingForbiddenAttributes;

public class PaAttributeValueAttributeRequestBuilderIetf implements
PaAttributeValueAttributeRequestBuilder {
	
	
	private long length;
	private List<AttributeReference> references;
	
	public PaAttributeValueAttributeRequestBuilderIetf(){
		this.length = 0;
		this.references = new LinkedList<>();
	}

	@Override
	public PaAttributeValueAttributeRequestBuilder addReferences(AttributeReference reference, AttributeReference... references) throws RuleException {
		
		List<AttributeReference> temp = new ArrayList<>();
		
		if(reference != null){
			temp.add(reference);
		}
		
		if(references != null){
			for (AttributeReference attributeReference : references) {
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
