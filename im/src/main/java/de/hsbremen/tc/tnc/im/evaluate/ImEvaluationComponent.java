package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public interface ImEvaluationComponent<T> {
	
	List<T> evaluate(ImSessionContext context);
	
	List<T> handle(List<? extends T> components, ImSessionContext context);
	
	List<T> lastCall(ImSessionContext context);
	
	void terminate();
}
