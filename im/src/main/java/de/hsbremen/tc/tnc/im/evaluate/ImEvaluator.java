package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public interface ImEvaluator {

	long getId();

	List<ImObjectComponent> evaluate(ImSessionContext context);
	
	List<ImObjectComponent> handle(ImObjectComponent component, ImSessionContext context);
	
	List<ImObjectComponent> lastCall(ImSessionContext context);
	
	void terminate();
}
