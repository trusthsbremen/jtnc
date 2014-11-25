package de.hsbremen.tc.tnc.im.evaluate;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.m.attribute.ImAttributeValue;

public abstract class AbstractImcEvaluationUnitIetf extends AbstractImEvaluationUnitIetf implements ImcEvaluationUnit{
	
	protected AbstractImcEvaluationUnitIetf(GlobalHandshakeRetryListener globalHandshakeRetryListener){
		super(globalHandshakeRetryListener);
	}
	
	@Override
	public synchronized List<ImAttribute> handle(List<? extends ImAttribute> attribute, ImSessionContext context) {
		List<ImAttribute> attributes = new LinkedList<>();
		for (ImAttribute imAttribute : attribute) {
			ImAttributeValue value = imAttribute.getValue();
			if(value instanceof PaAttributeValueAssessmentResult){
				attributes.addAll(this.handleResult((PaAttributeValueAssessmentResult) value, context));
			}else if( value instanceof PaAttributeValueRemediationParameters){
				attributes.addAll(this.handleRemediation((PaAttributeValueRemediationParameters) value, context));
			}else if(value instanceof PaAttributeValueError){
				attributes.addAll(this.handleError((PaAttributeValueError)value, context));
			}else if(value instanceof PaAttributeValueAttributeRequest){
				attributes.addAll(this.handleAttributeRequest((PaAttributeValueAttributeRequest)value, context));
			}
		}
		return attributes;
	}
	
	protected abstract List<? extends ImAttribute> handleAttributeRequest(
			PaAttributeValueAttributeRequest value, ImSessionContext context);

	protected abstract List<? extends ImAttribute> handleError(PaAttributeValueError value, ImSessionContext context);

	protected abstract List<? extends ImAttribute> handleRemediation(
			PaAttributeValueRemediationParameters value, ImSessionContext context);
	
	protected abstract List<? extends ImAttribute> handleResult(
			PaAttributeValueAssessmentResult value, ImSessionContext context);
	
	
	
	

}
