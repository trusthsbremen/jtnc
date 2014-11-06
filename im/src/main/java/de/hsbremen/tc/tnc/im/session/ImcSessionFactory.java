package de.hsbremen.tc.tnc.im.session;

import java.util.Map;

import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;

public interface ImcSessionFactory {

	public ImSession createSession(ImcConnectionAdapter connection,Map<Long,ImEvaluator> evaluators);
	
}
