package org.ietf.nea.pa.attribute;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

public interface PaAttributeValueAssessmentResultBuilder extends ImAttributeValueBuilder{
	
	public abstract PaAttributeValueAssessmentResultBuilder setResult(long result) throws RuleException;

}
