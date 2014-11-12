package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;

public interface ImSessionFactory<T extends ImSession> {

	public <S extends ImConnectionAdapter, E extends ImEvaluatorManager> T createSession(S connection, E manager);

	
}
