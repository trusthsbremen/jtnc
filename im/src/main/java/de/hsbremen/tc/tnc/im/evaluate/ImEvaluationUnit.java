package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public interface ImEvaluationUnit {
	
	long getVendorId();
	
	long getType(); 
	
	List<ImAttribute> evaluate(ImSessionContext context);
	
	List<ImAttribute> handle(List<? extends ImAttribute> attribute, ImSessionContext context);

	List<ImAttribute> lastCall(ImSessionContext context);
	
	void terminate();
}
