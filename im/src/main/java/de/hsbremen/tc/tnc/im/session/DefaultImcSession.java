package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;

public class DefaultImcSession extends AbstractDefaultImSession<ImcConnectionAdapter> implements ImcSession{ 
	
	DefaultImcSession(ImcConnectionAdapter connection, TncConnectionState connectionState, ImEvaluatorManager evaluator){
		super(connection, connectionState,evaluator);
	}
}
